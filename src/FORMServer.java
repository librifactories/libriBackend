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

	private void opListarProdutos(Query query, PrintStream body, Cliente cliente) {
	/*	JSONArray jsonProdutos =  new JSONArray();
		if (!cliente.getPedidoAtual().getProdutos().isEmpty()) {
			for (ItemPedido p : cliente.getPedidoAtual().getProdutos()) {
				jsonProdutos.put(p.toJson());
			}
		}
		body.println(jsonProdutos);
		*/

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

	public Cliente autenticarCliente(Query query) {
		Cliente retornar = null;
		for (Cliente cliente : clientes) {
			if (cliente.autenticacao(query.get("usuario"), query.get("senha"))) {
				retornar = cliente;
				break;
			}
		}
		return retornar;
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

			Cliente usuarioAtual = autenticarCliente(query);
			if (usuarioAtual != null) {

				switch (operacao) {
					case "novoProduto":
						opNovoProduto(query, body, usuarioAtual);
						break;
					case "listarProdutos":
						opListarProdutos(query, body, usuarioAtual);
						break;
					case "finalizarPedido":
						opFinalizarPedido(query, body, usuarioAtual);
						break;
					case "listarPedidos":
						opListarPedidos(query, body, usuarioAtual);
						break;
				}

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

		clientes.add(new Cliente("DarkPink", "Arvore"));

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