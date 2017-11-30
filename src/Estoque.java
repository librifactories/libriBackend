import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Estoque {
    private Queue<MateriaPrima> materias = new LinkedList<>();


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
