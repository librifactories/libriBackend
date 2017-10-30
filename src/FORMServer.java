import java.awt.Desktop;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public class FORMServer implements Container, Runnable {

	private static List<Cliente> clientes = new ArrayList<>();
	private static List<Funcionario> funcionarios = new ArrayList<>();
	private static ContainerSocketProcessor servidor;
	private static Connection conexao;

	private void opNovoProduto(Query query, PrintStream body, Cliente cliente) {
		JSONObject json = new JSONObject();
		try {
			cliente.getPedidoAtual().adicionarProduto(new ItemPedido(query.get("nome"), query.getFloat("preco"), query.get("material") ,query.get("corTampa"),query.get("corEmbalagem"),query.get("tipoTampa"), query.getInteger("quantidade")));
			json.put("status", "OK");
		}
		catch (NumberFormatException e) {
			json.put("status", "Erro");
			e.printStackTrace();
		}
		finally {
			json.put("operacao", "Novo produto");
			body.println(json);
		}
	}

	private void opListarItensCarrinho(Query query, PrintStream body, Cliente cliente) {
		JSONArray jsonProdutos =  new JSONArray();
		if (!cliente.getPedidoAtual().getProdutos().isEmpty()) {
			for (ItemPedido p : cliente.getPedidoAtual().getProdutos()) {
				jsonProdutos.put(p.toJson());
			}
		}
		body.println(jsonProdutos);
	}

	private void opListarPedidos(Query query, PrintStream body, Cliente cliente) {
		//pedidos.add(pedidoAtual);
		JSONArray jsonProdutos = new JSONArray();
		if (!cliente.getPedidos().isEmpty()) {
			for (Pedido p : cliente.getPedidos()) {
				jsonProdutos.put(p.toJson());
			}
		}
		body.println(jsonProdutos);
	}

	private void opFinalizarPedido(Query query, PrintStream body, Cliente cliente) {
		JSONObject json = new JSONObject();
		try {
			cliente.getPedidos().add(cliente.getPedidoAtual());
			cliente.setPedidoAtual(new Pedido());
			json.put("status", "OK");
		}
		catch (Exception e) {
			json.put("status", "Erro");
			e.printStackTrace();
		}
		finally {
			json.put("operacao", "Fechar");
			body.println(json);
		}
	}

	public Usuario autenticarUsuario(Query query) {
		Usuario retornar = null;
		for (Cliente cliente : clientes) {
			if (cliente.autenticacao(query.get("usuario"), query.get("senha"))) {
				retornar = cliente;
				break;
			}
		}
		for (Funcionario funcionario : funcionarios) {
			if (funcionario.autenticacao(query.get("usuario"), query.get("senha"))) {
				retornar = funcionario;
				break;
			}
		}
		return retornar;
	}

	public void opRealizarLogin(Query query, PrintStream body, Usuario usuario) {
		JSONObject json = new JSONObject();
		if (usuario.getNome() != null && usuario.getUsuario() != null && usuario.getSenha() != null) {
			json.put("nome", usuario.getNome());
			json.put("usuario", usuario.getUsuario());
			json.put("senha", usuario.getSenha());
			if (usuario instanceof Cliente) json.put("tipo", "1");
			else if (usuario instanceof Funcionario) json.put("tipo", "2-" + ((Funcionario) usuario).getCargo());
			body.println(json);
		}
	}

	public void opCadastrarCliente(Query query, PrintStream body) {
		String status = "ERRO";
		String usuario = query.get("usuario");
		String nome = query.get("nome");
		String sobrenome = query.get("sobrenome");
		String email = query.get("email");
		String senha = query.get("senha");
		Cliente c = new Cliente(usuario, senha, nome, sobrenome, email);
		if(!clientes.contains(c)) {
			clientes.add(c);
			status = "OK";
			System.out.println(c.getUsuario());
		}
		JSONObject jason = new JSONObject();
		jason.put("operacao", "cadastrarCliente");
		jason.put("status", status);
		body.println(jason);
	}

	public void handle(Request request, Response response) {
		try {
			if (LIBRIGUI.thread.isInterrupted()) {
				conexao.close();
				servidor.stop();
				return;
			}
			
			Query query = request.getQuery();
			PrintStream body = response.getPrintStream();
			long time = System.currentTimeMillis();

			//response.setValue("Content-Type", "text/plain");
			response.setValue("Content-Type", "application/json");
			response.setValue("Access-Control-Allow-Origin", "*");
			response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
			response.setDate("Date", time);
			response.setDate("Last-Modified", time);

			String operacao = query.get("operacao");
			if (operacao == null)
				operacao="";

			Usuario usuarioAtual;

				switch (operacao) {
					case "novoProduto":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null && usuarioAtual instanceof Cliente) opNovoProduto(query, body, (Cliente) usuarioAtual);
						break;
					case "listarItensCarrinho":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null && usuarioAtual instanceof Cliente) opListarItensCarrinho(query, body, (Cliente) usuarioAtual);
						break;
					case "finalizarPedido":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null && usuarioAtual instanceof Cliente) opFinalizarPedido(query, body, (Cliente) usuarioAtual);
						break;
					case "listarPedidos":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null && usuarioAtual instanceof Cliente) opListarPedidos(query, body, (Cliente) usuarioAtual);
						break;
					case "realizarLogin":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null) opRealizarLogin(query, body, usuarioAtual);
						break;
					case "cadastrarCliente":
						opCadastrarCliente(query, body);
						break;
				}

			body.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void iniciar() throws Exception {
		int porta = 880;

		// Configura uma conexão soquete para o servidor HTTP.
		Container container = new FORMServer();
		servidor = new ContainerSocketProcessor(container);
		conexao = new SocketConnection(servidor);
		SocketAddress endereco = new InetSocketAddress(porta);
		conexao.connect(endereco);

		clientes.add(new Cliente("DarkSider", "123", "Lucas", "Silveira", ""));
		clientes.add(new Cliente("PinkTree", "123", "Isabela","",""));
		clientes.add(new Cliente("Badaro15Br", "123", "Rafael","",""));
		funcionarios.add(new Funcionario("AprovarEncomendas", "123", "Brian", "AprovarEncomenda", "",""));
		funcionarios.add(new Funcionario("ControleEstoque", "123", "Igor", "ControleEstoque","",""));
		funcionarios.add(new Funcionario("ControlePedidos", "123", "Lucas", "ControlePedidos","",""));

		System.out.println("Tecle ENTER para interromper o servidor...");
		System.in.read();

		conexao.close();
		servidor.stop();

	}

	@Override
	public void run() {
		try {
			iniciar();
		}
		catch (Exception e) {

		}
	}
}