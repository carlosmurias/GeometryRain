package games.ameba.geometryrain;

public class Commons {
    /**
     * @return un valor aleatori entre 15 i 20 que, expressat en milisegons, fa que les shape amb una tasa de
     * refresc de 20 milisegons es moguin més lentes que les de 19, 18, etc. He considerat que per ara,
     * amb la mecànica actual de joc, velocitats per sobre de 15ms farien el joc injugable ja que
     * els ClickListener no son tan precissos.
     */
    public static int setPeriod() {
        int i = (int)Math.floor(Math.random() * 6);
        return 21 - i;
    }
}
