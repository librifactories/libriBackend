import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class Pedido implements JsonFormatter {

    private float precoTotal;
    private String id, situacao;
    private Calendar dataCompra, prazoEntrega;
    private List<ItemPedido> produtos = new ArrayList<>();

    public Pedido() {
        this.situacao = "Aguardando entrega";
        this.id = "#" + String.valueOf(new Random().nextInt(10000-1) + 1);
    }

    public void adicionarProduto(ItemPedido p) {
        this.produtos.add(p);
    }

    public List<ItemPedido> getProdutos() {
        return this.produtos;
    }

    public void calcularPrecoTotal() {
        this.precoTotal = 0;
        for (ItemPedido p : produtos) {
            this.precoTotal += p.getPrecoTotal();
        }
    }

    public void fecharPedido() {
        this.dataCompra = Calendar.getInstance();
        this.prazoEntrega = Calendar.getInstance();
        this.prazoEntrega.add(Calendar.MONTH, 1);
    }

    @Override
    public JSONObject toJson() {
        calcularPrecoTotal();
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("situacao", this.situacao);
        obj.put("precoTotal", this.precoTotal);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        obj.put("dataCompra", s.format(this.dataCompra.getTime()));
        obj.put("prazoEntrega", s.format(this.prazoEntrega.getTime()));
        JSONArray jsonProdutos = new JSONArray();
        for (ItemPedido p : this.produtos) {
            jsonProdutos.put(p.toJson());
        }
        obj.put("produtos", jsonProdutos);
        System.out.println(obj);
        return obj;
    }
}
