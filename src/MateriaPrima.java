import org.json.JSONObject;

public class MateriaPrima {

    private String nome;
    private int quantidade;
    private float preco;
    private boolean emEstoque;

    public MateriaPrima(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = 1;
    }

    public boolean estahEmEstoque() {
        return emEstoque;
    }

    public float getPreco() {
        return this.preco;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nome", this.nome);
        json.put("quantidade", this.quantidade);
        json.put("preco", this.preco);
        json.put("emEstoque", this.emEstoque);
        return json;
    }

}
