import org.json.JSONArray;
import org.json.JSONObject;

public class ItemPedido implements JsonFormatter {

    private Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public ItemPedido(String nome, float preco, String material,String corTampa,String corEmbalagem,String tipoTampa, int quantidade) {
        this.produto = new Produto(nome, preco, material, corTampa, corEmbalagem, tipoTampa);
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public float getPrecoTotal() {
        return this.quantidade * produto.getPreco();
    }

    public int getQuantidade() {
        return quantidade;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("produto", this.produto.toJson());
        obj.put("quantidade", this.quantidade);
        return obj;
    }
}
