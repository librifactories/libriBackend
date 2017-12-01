import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Produto implements JsonFormatter {

    private String nome;
    private float preco;
    private String material;
    private List<MateriaPrima> materiasPrimas = new ArrayList<>();
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
    }

    public static void inicializar() {
        temposMedios.put("Caixas K", 20F);
        temposMedios.put("Pouche com Ziper 250g", 30F);
        temposMedios.put("Pouche sem Ziper 250g", 30F);
        temposMedios.put("Flaconete 1ml", 20F);
        temposMedios.put("Flaconete 4ml", 20F);
        temposMedios.put("Flaconete 10ml", 20F);
        temposMedios.put("Frasco 250ml", 25F);
        temposMedios.put("Frasco 100ml", 25F);
        temposMedios.put("Potinho 100g", 25F);
        temposMedios.put("Potinho 150g", 25F);
        temposMedios.put("Frasco para Aromatizador 100ml", 4F);
    }

    public boolean faltaMateriaPrima() {
        for (MateriaPrima mp : materiasPrimas) {
            if (!mp.estahEmEstoque())
                return true;
        }
        return false;
    }

    public boolean faltaMateriaPrima(String nome) {
        for (MateriaPrima mp : materiasPrimas) {
            if (mp.getNome().equals(nome) && !mp.estahEmEstoque())
                return true;
        }
        return false;
    }

    public Produto(String nome){
        this.nome = nome;
    }

    public String getNome() {return this.nome; }

    public float getPreco() {return this.preco; }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public List<MateriaPrima> getMateriasPrimas() {
        return materiasPrimas;
    }

    public String getMaterial(){return this.material; }

    public String getCorTampa() { return this.corTampa; }

    public String getCorEmbalagem() { return corEmbalagem; }

    public String getTipoTampa() { return tipoTampa; }

    public Float getTempoMedio() {
        return temposMedios.get(this.nome);
    }

    public void setTempoMedio(float tempoMedio) { temposMedios.replace(this.nome, tempoMedio); }

    public float getCustoProducao() {
        float custo = 0;
        for (MateriaPrima mp : materiasPrimas) {
            custo += mp.getPreco() * mp.getQuantidade();
        }
        return custo;
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
        JSONArray arr = new JSONArray();
        for (MateriaPrima mp : materiasPrimas) {
            arr.put(mp.toJson());
        }
        obj.put("materiasPrimas", arr);
        obj.put("custoProducao", this.getCustoProducao());
        return obj;
    }

}
