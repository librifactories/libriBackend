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
        this.data = Calendar.getInstance();
    }

    public Item(String nome, String material, String corTampa, String corEmbalagem, String tipoTampa, int quantidade) {
        this.produto = new Produto(nome, material, corTampa, corEmbalagem, tipoTampa);
        this.quantidade = quantidade;
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

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("produto", this.produto.toJson());
        obj.put("quantidade", this.quantidade);
        obj.put("finalizado", this.finalizado);
        return obj;
    }
}
