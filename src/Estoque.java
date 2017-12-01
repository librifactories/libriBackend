import java.util.*;

public class Estoque {
    private List<MateriaPrima> materias = new ArrayList<>();


    private boolean setProdutoJaExistente(MateriaPrima novoProduto){
        boolean encontrou = false;
        Iterator it = materias.iterator();
        if(!materias.isEmpty()){
        while(it.hasNext()){
            MateriaPrima essa = (MateriaPrima) it.next();
            if(novoProduto.getNome().equals(essa.getNome())){
                encontrou = true;
            }
        }
        }
        return encontrou;
    }

    public void add(MateriaPrima novoProduto){
        if(!setProdutoJaExistente(novoProduto)) {
            materias.add(novoProduto);
        }
    }

    public boolean contem(String nome, int quantidade) {
        for (MateriaPrima p : materias) {
            if (p.getNome().equals(nome) && p.getQuantidade() >= quantidade)
                return true;
        }
        return false;
    }

    public boolean reduzirQuantidade(String nome, int quantidade) {
        if (contem(nome, quantidade)) {
            for (MateriaPrima p : materias) {
                if (p.getNome().equals(nome)) {
                    p.reduzirQuantidade(quantidade);
                    return true;
                }
            }
        }
        return false;
    }

    public void delAll(){
        materias.clear();
    }

    public void dellIndex(Item i){
        materias.remove(i);
    }

    public MateriaPrima[] toArray(){
        int tamArray = materias.size();
        MateriaPrima[] itens = new MateriaPrima[tamArray];
        materias.toArray(itens);
        return itens;
    }

}
