import org.json.JSONObject;

public class MateriaPrima {
    private String nome;
    private int quant, min, max;

    public MateriaPrima(String nome, int quant){
        this.nome = nome;
        this.quant = quant;
    }
    public MateriaPrima(String nome, int quant, int min, int max){
        this.nome = nome;
        this.quant = quant;
        this.min = min;
        this.max = max;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("nome", this.nome);
        obj.put("quantidade", this.quant);
        return obj;
    }
}
