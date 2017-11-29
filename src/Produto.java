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

    public Produto(String nome,String material,String corTampa,String corEmbalagem,String tipoTampa) {
        this.nome = nome;
        this.preco = preco;
        this.material = material;
        this.corTampa = corTampa;
        this.corEmbalagem = corEmbalagem;
        this.tipoTampa = tipoTampa;
        setInformacoes();
    }

    private void setInformacoes() {
        switch (this.nome) {
            case "Caixas K":
                this.preco = 10F;
                this.tempoMedio = 2F;
                break;
            case "Pouche com Ziper 250g":
                this.preco = 5F;
                this.tempoMedio = 2F;
                break;
            case "Pouche sem Ziper 250g":
                this.preco = 3F;
                this.tempoMedio = 2F;
                break;
            case "Flaconete 1ml":
                this.preco = 1F;
                this.tempoMedio = 2F;
                break;
            case "Flaconete 4ml":
                this.preco = 3F;
                this.tempoMedio = 2F;
                break;
            case "Flaconete 10ml":
                this.preco = 5F;
                this.tempoMedio = 2F;
                break;
            case "Frasco 250ml":
                this.preco = 2F;
                this.tempoMedio = 2F;
                break;
            case "Frasco 100ml":
                this.preco = 2F;
                this.tempoMedio = 2F;
                break;
            case "Potinho 100g":
                this.preco = 1F;
                this.tempoMedio = 2F;
                break;
            case "Potinho 150g":
                this.preco = 2F;
                this.tempoMedio = 2F;
                break;
            case "Frasco para Aromatizador 100ml":
                this.preco = 2.0F;
                this.tempoMedio = 2F;
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
        return tempoMedio;
    }

    public void setTempoMedio(float tempoMedio) {
        this.tempoMedio = tempoMedio;
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
