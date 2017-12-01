import java.util.*;

public class Estoque {
    private List<MateriaPrima> materias = new ArrayList<>();


    private boolean setProdutoJaExistente(MateriaPrima novoProduto){
        boolean encontrou = false;
        Iterator it = materias.iterator();
        if(!materias.isEmpty()){
        while(it.hasNext()){
            MateriaPrima essa = (MateriaPrima) it.next();
            if(novoProduto.getNome().toUpperCase().equals(essa.getNome())){
                encontrou = true;
                essa.addQuant(novoProduto.getQuant());
            }
        }
        }
        return encontrou;
    }

    public List<MateriaPrima> getMaterias() {
        return materias;
    }

    public void add(MateriaPrima novoProduto){
        if(!setProdutoJaExistente(novoProduto)) {
            materias.add(novoProduto);
        }
    }

    public void del(MateriaPrima novoProduto){
        Iterator it = materias.iterator();
        while(it.hasNext()){
            MateriaPrima essa = (MateriaPrima) it.next();
            if(novoProduto.getNome().toUpperCase().equals(essa.getNome())){
                essa.delQuant(novoProduto.getQuant());
                    if(essa.getQuant() <= 0) it.remove();
            }
        }
    }

    public boolean contem(String nome, int quantidade) {
        for (MateriaPrima p : materias) {
            if (p.getNome().equals(nome) && p.getQuantidade() >= quantidade)
                return true;
        }
        return false;
    }

    public boolean estahEmEstoque(String nome) {
        for (MateriaPrima p : materias) {
            if (p.getNome().equals(nome))
                return p.estahEmEstoque();
        }
        return false;
    }

    public void setEmEstoque(String nome, boolean emEstoque) {
        for (MateriaPrima p : materias) {
            if (p.getNome().equals(nome))
                p.setEmEstoque(emEstoque);
        }
    }

    public boolean reduzirQuantidade(String nome, int quantidade) {
        for (MateriaPrima p : materias) {
            if (p.getNome().equals(nome)) {
                System.out.println(p.getQuantidade() - quantidade);
                return p.reduzirQuantidade(quantidade);
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
