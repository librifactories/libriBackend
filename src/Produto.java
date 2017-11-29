import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Produto implements JsonFormatter {

    private String nome;
    private float preco;
    private String material;
    private String corTampa;
    private String corEmbalagem;
    private String tipoTampa;
    private Float tempoMedio;
    private static Map<String, Float> temposMedios = new HashMap<>();

    public Produto(String nome,String material,String corTampa,String corEmbalagem,String tipoTampa) {
        this.nome = nome;
        this.preco = preco;
        this.material = material;
        this.corTampa = corTampa;
        this.corEmbalagem = corEmbalagem;
        this.tipoTampa = tipoTampa;
        this.tempoMedio = temposMedios.get(this.nome);
        this.setInformacoes();
    }

    public static void inicializar() {
        temposMedios.put("Caixas K", 2F);
        temposMedios.put("Pouche com Ziper 250g", 3F);
        temposMedios.put("Pouche sem Ziper 250g", 3F);
        temposMedios.put("Flaconete 1ml", 2F);
        temposMedios.put("Flaconete 4ml", 2F);
        temposMedios.put("Flaconete 10ml", 2F);
        temposMedios.put("Frasco 250ml", 2F);
        temposMedios.put("Frasco 100ml", 2F);
        temposMedios.put("Potinho 100g", 2F);
        temposMedios.put("Potinho 150g", 2F);
        temposMedios.put("Frasco para Aromatizador 100ml", 4F);
    }

    private void setInformacoes() {
        switch (this.nome) {
            case "Caixas K":
                this.preco = 10F;
                break;
            case "Pouche com Ziper 250g":
                this.preco = 5F;
                break;
            case "Pouche sem Ziper 250g":
                this.preco = 3F;
                break;
            case "Flaconete 1ml":
                this.preco = 1F;
                break;
            case "Flaconete 4ml":
                this.preco = 3F;
                break;
            case "Flaconete 10ml":
                this.preco = 5F;
                break;
            case "Frasco 250ml":
                this.preco = 2F;
                break;
            case "Frasco 100ml":
                this.preco = 2F;
                break;
            case "Potinho 100g":
                this.preco = 1F;
                break;
            case "Potinho 150g":
                this.preco = 2F;
                break;
            case "Frasco para Aromatizador 100ml":
                this.preco = 2.0F;
                break;
        }
    }

    public String getNome() {return this.nome; }

    public float getPreco() {return this.preco; }

    public String getMaterial(){return this.material; }

    public String getCorTampa() { return this.corTampa; }

    public String getCorEmbalagem() { return corEmbalagem; }

    public String getTipoTampa() { return tipoTampa; }

    public Float getTempoMedio() {
        return temposMedios.get(this.nome);
    }

    public void setTempoMedio(float tempoMedio) {
        temposMedios.replace(this.nome, tempoMedio);
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("nome", this.nome);
        obj.put("preco", this.preco);
        obj.put("material", this.material);
        obj.put("corTampa", this.corTampa);
        obj.put("corEmbalagem", this.corEmbalagem);
        obj.put("tipoTampa", this.tipoTampa);
        obj.put("tempoMedio", this.tempoMedio);
        return obj;
    }

}
