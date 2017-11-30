import org.json.JSONArray;
import org.json.JSONObject;

public class Item implements JsonFormatter {

    private Produto produto;

    private int quantidade;

    public Item(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Item(String nome, float preco, String material, String corTampa, String corEmbalagem, String tipoTampa, int quantidade) {
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


    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("produto", this.produto.toJson());
        obj.put("quantidade", this.quantidade);
        return obj;
    }
}
