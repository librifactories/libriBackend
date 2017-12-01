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
        if (this.quantidade > 0) this.emEstoque = true;
        else this.emEstoque = false;
    }

    public boolean estahEmEstoque() {
        //return FORMServer.estoque.contem(this.nome, this.quantidade);
        return emEstoque;
    }

    public void setEmEstoque(boolean emEstoque) {
        this.emEstoque = emEstoque;
    }

    public boolean reduzirQuantidade(int quantidade) {
        if (this.quantidade - quantidade >= 0) {
            this.quantidade -= quantidade;
            return true;
        }
        return false;
    }

    public float getPreco() {
        return this.preco;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public String getNome(){
        return this.nome;
    }

    public int getQuant(){
        return this.quantidade;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nome", this.nome);
        json.put("quantidade", this.quantidade);
        json.put("preco", this.preco);
        json.put("emEstoque", this.estahEmEstoque());
        return json;
    }
}
