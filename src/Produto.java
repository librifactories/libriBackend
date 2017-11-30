import org.json.JSONObject;

public class Produto implements JsonFormatter {

    private String nome;
    private float preco;
    private String material;
    private String corTampa;
    private String corEmbalagem;
    private String tipoTampa;


    public Produto(String nome, float preco,String material,String corTampa,String corEmbalagem,String tipoTampa) {
        this.nome = nome;
        this.preco = preco;
        this.material = material;
        this.corTampa = corTampa;
        this.corEmbalagem = corEmbalagem;
        this.tipoTampa = tipoTampa;
    }

    public Produto(String nome){
        this.nome = nome;
    }

    public String getNome() {return this.nome; }

    public float getPreco() {return this.preco; }

    public String getMaterial(){return this.material; }

    public String getCorTampa() { return this.corTampa; }

    public String getCorEmbalagem() { return corEmbalagem; }

    public String getTipoTampa() { return tipoTampa; }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("nome", this.nome);
        obj.put("preco", this.preco);
        obj.put("material", this.material);
        obj.put("corTampa",this.corTampa);
        obj.put("corEmbalagem",this.corEmbalagem);
        obj.put("tipoTampa",this.tipoTampa);
        return obj;
    }

}
