import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Item implements JsonFormatter {

    private Produto produto;
    private int quantidade;
    private Calendar data;
    private boolean finalizado;

    public Item(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Item(String nome, String material, String corTampa, String corEmbalagem, String tipoTampa, int quantidade) {
        this.produto = new Produto(nome, material, corTampa, corEmbalagem, tipoTampa);
        this.quantidade = quantidade;
        this.data = Calendar.getInstance();
        this.setInformacoes();
    }

    //SOCORRO SOCORRO SOCORRO SOCORRO SOCORRO
    //ACABOU O CAFÉ ACABOU O CAFÉ ACABOU O CAFÉ

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

    public void iniciouProducao() {
        this.data = Calendar.getInstance();
    }

    public void finalizouProducao() {
        Float tempoMedio = this.produto.getTempoMedio();
        Date data = this.data.getTime();
        long DAY = 24L * 60L * 60L * 1000L;
        float diferenca = (( data.getTime() - Calendar.getInstance().getTime().getTime() ) / DAY);
        if (tempoMedio == null) {
            this.produto.setTempoMedio(diferenca);
        } else {
            this.produto.setTempoMedio((this.produto.getTempoMedio() + diferenca) / 2);
        }
        this.finalizado = true;
        System.out.println("Tempo Médio de produção desse produto lindinho: " + this.produto.getTempoMedio());
    }

    private void setInformacoes() {
        switch (this.produto.getNome()) {
            case "Caixas K":
                this.produto.setPreco(10F);
                break;
            case "Pouche com Ziper 250g":
                this.produto.setPreco(5F);
                break;
            case "Pouche sem Ziper 250g":
                this.produto.setPreco(3F);
                break;
            case "Flaconete 1ml":
                this.produto.setPreco(1F);
                break;
            case "Flaconete 4ml":
                this.produto.setPreco(3F);
                break;
            case "Flaconete 10ml":
                this.produto.setPreco(5F);
                break;
            case "Frasco 250ml":
                this.produto.setPreco(2F);
                break;
            case "Frasco 100ml":
                this.produto.setPreco(2F);
                break;
            case "Potinho 100g":
                this.produto.setPreco(1F);
                break;
            case "Potinho 150g":
                this.produto.setPreco(2F);
                break;
            case "Frasco para Aromatizador 100ml":
                this.produto.setPreco(2.0F);
                break;
        }
        switch (this.produto.getMaterial()) {
            case "PET - Polietileno tereftalato":
                this.produto.getMateriasPrimas().add(new MateriaPrima("PET", 3 * this.quantidade));
                break;
            case "PVC - Policloreto de polivinila":
                this.produto.getMateriasPrimas().add(new MateriaPrima("PVC", 4 * this.quantidade));
                break;
            case "PP - Polipropileno":
                this.produto.getMateriasPrimas().add(new MateriaPrima("Polipropileno", 2 * this.quantidade));
                break;
            case "PS - Poliestireno":
                this.produto.getMateriasPrimas().add(new MateriaPrima("Poliestireno", 5 * this.quantidade));
                break;
            case "EVA - Acetato-vinilo de etileno":
                this.produto.getMateriasPrimas().add(new MateriaPrima("EVA", 10 * this.quantidade));
                break;
        }
        switch (this.produto.getTipoTampa()) {
            case "Tampa de encaixe":
                this.produto.getMateriasPrimas().add(new MateriaPrima("Tampa de encaixe", 2 * this.quantidade));
                break;
            case "Tampa de rosca com spray":
                this.produto.getMateriasPrimas().add(new MateriaPrima("Tampa de rosca com spray", 3 * this.quantidade));
                break;
            case "Tampa de rosca com contador":
                this.produto.getMateriasPrimas().add(new MateriaPrima("Tampa de rosca com contador", 4 * this.quantidade));
                break;
            case "Tampa de rosca":
                this.produto.getMateriasPrimas().add(new MateriaPrima("Tampa de rosca", 5 * this.quantidade));
                break;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("produto", this.produto.toJson());
        obj.put("quantidade", this.quantidade);
        obj.put("finalizado", this.finalizado);
        return obj;
    }
}
