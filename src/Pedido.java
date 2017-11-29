import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class Pedido implements JsonFormatter {

    private float precoTotal;
    private String id, situacao;
    private Calendar dataCompra, prazoEntrega;
    private List<Item> produtos = new ArrayList<>();

    public Pedido() {
        this.situacao = "Aguardando entrega";
        this.setPrazoEntrega();
        this.id = "#" + String.valueOf(new Random().nextInt(10000-1) + 1);
    }

    public String getId() {
        return this.id;
    }

    public void adicionarProduto(Item p) {
        this.produtos.add(p);
    }

    public List<Item> getProdutos() {
        return this.produtos;
    }

    private void setPrazoEntrega() {
        float tempoEstimado = 0;
        for (Item i : this.getProdutos()) {
            tempoEstimado = Math.max(tempoEstimado, i.getProduto().getTempoMedio());
        }
        prazoEntrega = Calendar.getInstance();
        prazoEntrega.add(Calendar.DATE, (int) tempoEstimado);
    }

    public void calcularPrecoTotal() {
        this.precoTotal = 0;
        for (Item p : produtos) {
            this.precoTotal += p.getPrecoTotal();
        }
    }

    public void fecharPedido() {
        this.dataCompra = Calendar.getInstance();
        setPrazoEntrega();
    }

    public Calendar getPrazoEntrega() {
        return prazoEntrega;
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
        for (Item p : this.produtos) {
            jsonProdutos.put(p.toJson());
        }
        obj.put("produtos", jsonProdutos);
        System.out.println(obj);
        return obj;
    }
}
