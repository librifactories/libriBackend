import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Pedido implements JsonFormatter {

    private float precoTotal;
    private String id, situacao;
    private Calendar dataCompra, prazoEntrega;
    private List<Item> produtos = new ArrayList<>();

    public Pedido() {
        this.situacao = "Aguardando entrega";
        this.setPrazoEntrega();
        this.id = "#" + String.valueOf(new Random().nextInt(10000-1) + 1);
    }

    public String getId() {
        return this.id;
    }

    public void adicionarProduto(Item p) {
        this.produtos.add(p);
    }

    public List<Item> getProdutos() {
        return this.produtos;
    }

    public float getCustoProducao() {
        float custo = 0;
        for (Item i : produtos) {
            custo += i.getProduto().getCustoProducao() * i.getQuantidade();
        }
        return custo;
    }

    public float getLucroEsperado() {
        float lucro = 0;
        for (Item i : produtos) {
            lucro += i.getProduto().getPreco() * i.getQuantidade();
        }
        lucro -= getCustoProducao();
        return lucro;
    }

    private void setPrazoEntrega() {
        float tempoEstimado = 0;
        for (Item i : this.getProdutos()) {
            tempoEstimado = Math.max(tempoEstimado, i.getProduto().getTempoMedio());
        }
        prazoEntrega = Calendar.getInstance();
        prazoEntrega.add(Calendar.DATE, (int) tempoEstimado);
    }

    private int getTempoEstimado() {
        float maiorTempo = 0;
        for (Item i : produtos) {
            if (i.getProduto().getTempoMedio() > maiorTempo)
                maiorTempo = i.getProduto().getTempoMedio();
        }
        return (int) maiorTempo;
    }

    public void calcularPrecoTotal() {
        this.precoTotal = 0;
        for (Item p : produtos) {
            this.precoTotal += p.getPrecoTotal();
        }
    }

    public void fecharPedido() {
        this.dataCompra = Calendar.getInstance();
        this.prazoEntrega = Calendar.getInstance();
        this.prazoEntrega.add(Calendar.MONTH, 1);
        for (Item i : this.produtos) {
            for (MateriaPrima mp : i.getProduto().getMateriasPrimas()) {
                if (FORMServer.estoque.reduzirQuantidade(mp.getNome(), mp.getQuantidade()))
                    FORMServer.estoque.setEmEstoque(mp.getNome(), true);
                else FORMServer.estoque.setEmEstoque(mp.getNome(), false);
            }
        }
        setPrazoEntrega();
    }

    private boolean faltaMateriaPrima() {
        for (Item i : produtos) {
            for (MateriaPrima mp : i.getProduto().getMateriasPrimas()) {
                if (!FORMServer.estoque.estahEmEstoque(mp.getNome()))
                    return true;
            }
        }
        return false;
    }

    public Calendar getPrazoEntrega() {
        return prazoEntrega;
    }

    @Override
    public JSONObject toJson() {
        calcularPrecoTotal();
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("situacao", this.situacao);
        obj.put("precoTotal", this.precoTotal);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        obj.put("dataCompra", s.format(this.dataCompra.getTime()));
        obj.put("prazoEntrega", s.format(this.prazoEntrega.getTime()));
        JSONArray jsonProdutos = new JSONArray();
        for (Item p : this.produtos) {
            jsonProdutos.put(p.toJson());
        }
        obj.put("produtos", jsonProdutos);
        obj.put("custoProducao", this.getCustoProducao());
        obj.put("lucroEsperado", this.getLucroEsperado());
        obj.put("tempoEstimado", this.getTempoEstimado());
        obj.put("faltaMateriaPrima", this.faltaMateriaPrima());
        System.out.println(obj);
        return obj;
    }
}
