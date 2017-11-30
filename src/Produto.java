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

    public boolean faltaMateriaPrima() {
        for (MateriaPrima mp : materiasPrimas) {
            if (!mp.estahEmEstoque())
                return true;
        }
        return false;
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
        switch (this.material) {
            case "PET - Polietileno tereftalato":
                this.materiasPrimas.add(new MateriaPrima("PET", 3));
                break;
            case "PVC - Policloreto de polivinila":
                this.materiasPrimas.add(new MateriaPrima("PVC", 4));
                break;
            case "PP - Polipropileno":
                this.materiasPrimas.add(new MateriaPrima("Polipropileno", 2));
                break;
            case "PS - Poliestireno":
                this.materiasPrimas.add(new MateriaPrima("Poliestireno", 5));
                break;
            case "EVA - Acetato-vinilo de etileno":
                this.materiasPrimas.add(new MateriaPrima("EVA", 10));
                break;
        }
        switch (this.tipoTampa) {
            case "Tampa de encaixe":
                this.materiasPrimas.add(new MateriaPrima("Tampa de encaixe", 1));
                break;
            case "Tampa de rosca com spray":
                this.materiasPrimas.add(new MateriaPrima("Tampa de rosca com spray", 1));
                break;
            case "Tampa de rosca com contador":
                this.materiasPrimas.add(new MateriaPrima("Tampa de rosca com contador", 1));
                break;
            case "Tampa de rosca":
                this.materiasPrimas.add(new MateriaPrima("Tampa de rosca", 1));
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
