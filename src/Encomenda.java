import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Encomenda implements JsonFormatter {

    private String id, situacao;
    private Calendar dataPedido;
    private List<Item> produtos = new ArrayList<>();
    private float precoTotal;

    public Encomenda() {
        this.id = "#" + String.valueOf(new Random().nextInt(10000-1) + 1);
        this.situacao = "Aguardando aprovacao";
        this.dataPedido = Calendar.getInstance();
    }

    public void adicionarProduto(Item p) {
        this.produtos.add(p);
    }

    public List<Item> getProdutos() {
        return this.produtos;
    }

    public void calcularPrecoTotal() {
        this.precoTotal = 0;
        for (Item p : produtos) {
            this.precoTotal += p.getPrecoTotal();
        }
    }

    public String getId() {
        return id;
    }

    public String getSituacao() {
        return situacao;
    }

    public Calendar getDataPedido() {
        return dataPedido;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    @Override
    public JSONObject toJson(){
        calcularPrecoTotal();
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("situacao", this.situacao);
        obj.put("precoTotal", this.precoTotal);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        obj.put("dataPedido", s.format(this.dataPedido.getTime()));
        JSONArray jsonProdutos = new JSONArray();
        for (Item p : this.produtos) {
            jsonProdutos.put(p.toJson());
        }
        obj.put("produtos", jsonProdutos);
        System.out.println(obj);
        return obj;
    }
}
