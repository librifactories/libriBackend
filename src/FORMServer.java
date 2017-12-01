import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
	private static Estoque estoque = new Estoque();
	private static List<Cliente> clientes = new ArrayList<>();
	private static List<Funcionario> funcionarios = new ArrayList<>();
	private static List<Pedido> pedidosAProduzir = new ArrayList<>();
	private static List<Encomenda> encomendas = new ArrayList<>();

	private static ContainerSocketProcessor servidor;
	private static Connection conexao;

	private void opNovoProduto(Query query, PrintStream body, Cliente cliente) {
		JSONObject json = new JSONObject();
		try {
			cliente.getPedidoAtual().adicionarProduto(new Item(query.get("nome"), query.get("material"), query.get("corTampa"),query.get("corEmbalagem"),query.get("tipoTampa"), query.getInteger("quantidade")));
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
			for (Item p : cliente.getPedidoAtual().getProdutos()) {
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

	private void opListarPedidos(Query query, PrintStream body) {
		//pedidos.add(pedidoAtual);
		JSONArray jsonProdutos = new JSONArray();
		if (!pedidosAProduzir.isEmpty()) {
			for (Pedido p : pedidosAProduzir) {
				jsonProdutos.put(p.toJson());
			}
		}
		body.println(jsonProdutos);
	}

	private void opFinalizarPedido(Query query, PrintStream body, Cliente cliente) {
		JSONObject json = new JSONObject();
		try {
			for (Item i : cliente.getPedidoAtual().getProdutos()) {
				i.iniciouProducao();
			}
			cliente.getPedidoAtual().fecharPedido();
			cliente.getPedidos().add(cliente.getPedidoAtual());
			pedidosAProduzir.add(cliente.getPedidoAtual());
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

	private void opListarEncomendas(Query query, PrintStream body){
		JSONArray jsonEncomendas = new JSONArray();
		if(!encomendas.isEmpty()) {
			for (Encomenda e : encomendas){
				jsonEncomendas.put(e.toJson());
			}
		}
		body.println(jsonEncomendas);
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

	public void opAtualizarEstoque(Query query, PrintStream body){
		JSONArray pARR = new JSONArray();
        JSONObject json = new JSONObject();
        MateriaPrima materias[] = estoque.toArray();
		for(int i = 0; i<materias.length; i++){
			json.put("nome", materias[i].getNome());
			json.put("quantidade", materias[i].getQuant());
			pARR.put(json);
			json = new JSONObject();
		}
        body.println(pARR);
    }

	public void opAddProdutosEstoque(Query query, PrintStream body){
        String nome = query.get("nome");
        String quant = query.get("quantidade");
        int quantidade = Integer.parseInt(quant);
        MateriaPrima novaMateria = new MateriaPrima(nome, quantidade);
        estoque.add(novaMateria);
    }
	public void opProdutoFinalizado(Query query, PrintStream body, Funcionario funcionario) {
		JSONObject json = new JSONObject();
		json.put("operacao", "finalizarProduto");
		try {
			loop1:
			for (Pedido p : pedidosAProduzir) {
				if (p.getId().contains(query.get("id"))) {
					for (Item i : p.getProdutos()) {
						if (i.getProduto().getNome().concat(" ").equals(query.get("nome"))) {
							json.put("status", "Produto finalizado com sucesso");
							i.finalizouProducao();
							break loop1;
						} else {
							System.out.println(query.get("nome") + " - " + i.getProduto().getNome());
						}
					}
				}
			}
		} catch(Exception e) {
			json.put("status", "ERRO");
		}
		body.println(json);
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
						if (usuarioAtual != null) {
							if (usuarioAtual instanceof Cliente) {
								opListarPedidos(query, body, (Cliente) usuarioAtual);
							}
							else if (usuarioAtual instanceof Funcionario) {
								opListarPedidos(query, body);
							}
						}
						break;
					case "realizarLogin":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null) opRealizarLogin(query, body, usuarioAtual);
						break;
					case "produtoFinalizado":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null) opProdutoFinalizado(query, body, (Funcionario) usuarioAtual);
						break;
					case "listarEncomendas":
						usuarioAtual = autenticarUsuario(query);
						if (usuarioAtual != null && usuarioAtual instanceof Funcionario) opListarEncomendas(query, body);
						break;
					case "cadastrarCliente":
						opCadastrarCliente(query, body);
						break;
					case "atualizarEstoque":
						opAtualizarEstoque(query, body);
						break;
                    case "addProdutosEstoque":
				        opAddProdutosEstoque(query, body);
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

		Produto.inicializar();
		encomendas.add(new Encomenda());
		encomendas.add(new Encomenda());
		encomendas.add(new Encomenda());

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