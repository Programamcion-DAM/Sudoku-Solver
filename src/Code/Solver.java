package Code;

public class Solver {

    private final int row = 9;

    public char[][] getSolution(char[][] sudoku) {
        char[][] lastSudoku = new char[row][row];
        do {

            char[][][] sudokuRows = getSudokuRow(sudoku);
            char[][][] sudokuColumns = getSudokuColumns(sudoku);
            char[][][] sudokuSquares = getSudokuSquares(sudoku);
            
            char[][][] mixSudoku = getMixSudoku(sudoku, sudokuRows, sudokuColumns, sudokuSquares);
            
            chooseByOcupations(sudoku, mixSudoku);
            chooseByLimit(sudoku, mixSudoku);
            
            if(checkValidSudoku(sudoku) == false){
                return null;
            }
            if(equals(sudoku,lastSudoku)){
                return sudokuRandomMethod(sudoku,mixSudoku);
            }else{
                copy(sudoku,lastSudoku);
            }
        }while(checkGameOver(sudoku) == false);
        
        return sudoku;
    
    }

    private char[][][] getSudokuRow(char s[][]) {
        //Metodo que va fila por fila viendo cual numero cabría en cada casilla.
        //Te devuelve una matriz con todas las disitntas posibilidades
        char[][][] matrix = new char[row][row][row];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < row; k++) {
                    matrix[i][j][k] = '0';
                }
            }
        }

        for (int i = 0; i < row; i++) {
            char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
            for (int j = 0; j < row; j++) {
                //Recorremos el sudoku por filas y cada vez que vemos un numero lo eliminamos de la lista de numeros
                //Posteriormente, esa lista la introducimos en un sudoku 3D en el que el eje Z serán las posibles combinaciones.
                if (s[i][j] != '0') {
                    deleteNumberOfArray(s[i][j], numberArray);
                }
            }

            for (int j = 0; j < row; j++) {
                //Mira a ver si en el sudoku esta la casilla llena, si no es así, la llenamos con los posibles números
                if (s[i][j] == '0') {
                    for (int k = 0; k < row; k++) {
                        matrix[i][j][k] = numberArray[k];
                    }
                } else {
                    matrix[i][j][0] = s[i][j];
                }
            }
        }
        return matrix;
    }

    private char[][][] getSudokuColumns(char s[][]) {
        //Metodo que va columna por columna viendo cual numero cabría en cada casilla.
        //Te devuelve una matriz con todas las disitntas posibilidades
        char[][][] matrix = new char[row][row][row];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < row; k++) {
                    matrix[i][j][k] = '0';
                }
            }
        }

        for (int i = 0; i < row; i++) {
            char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
            for (int j = 0; j < row; j++) {
                //Recorremos el sudoku por columna y cada vez que vemos un numero lo eliminamos de la lista de numeros
                //Posteriormente, esa lista la introducimos en un sudoku 3D en el que el eje Z serán las posibles combinaciones.
                if (s[j][i] != '0') {
                    deleteNumberOfArray(s[j][i], numberArray);
                }
            }

            for (int j = 0; j < row; j++) {
                //Mira a ver si en el sudoku esta la casilla llena, si no es así, la llenamos con los posibles números
                if (s[j][i] == '0') {
                    for (int k = 0; k < row; k++) {
                        matrix[j][i][k] = numberArray[k];
                    }
                } else {
                    matrix[j][i][0] = s[j][i];
                }
            }
        }
        return matrix;
    }

    private char[][][] getSudokuSquares(char s[][]) {
        //Metodo que va cuadrado por cuadrado viendo cual numero cabría en cada casilla.
        //Te devuelve una matriz con todas las disitntas posibilidades
        char[][][] matrix = new char[row][row][row];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < row; k++) {
                    matrix[i][j][k] = '0';
                }
            }
        }

        for (int iSqr = 0; iSqr < row; iSqr += 3) {
            for (int jSqr = 0; jSqr < row; jSqr += 3) {

                char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

                //Eliminamos todos los numeros que esten en el cuadrante
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (s[i + iSqr][j + jSqr] != '0') {
                            deleteNumberOfArray(s[i + iSqr][j + jSqr], numberArray);
                        }
                    }
                }

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (s[i + iSqr][j + jSqr] == '0') {
                            for (int k = 0; k < row; k++) {
                                matrix[i + iSqr][j + jSqr][k] = numberArray[k];
                            }
                        } else {
                            matrix[i + iSqr][j + jSqr][0] = s[i + iSqr][j + jSqr];
                        }
                    }
                }
            }
        }
        return matrix;
    }

    private void deleteNumberOfArray(char c, char[] arr) {
        //Metodo que elimina el caracter variable c del array.
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c) {
                arr[i] = '0';
            }
        }
    }

    private char[][][] getMixSudoku(char[][] sudoku, char[][][] sudokuRow, char[][][] sudokuColumns, char[][][] sudokuSquares) {
        char[][][] matrix = new char[row][row][row];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < row; k++) {
                    matrix[i][j][k] = '0';
                }
            }
        }
        
        checkBlockNumbers(sudoku, sudokuRow, sudokuColumns);
        
        for(int i = 0; i < row;i++){
            for(int j = 0;j < row;j++){
                
                for(int k1 = 0;k1 < row;k1++){
                    for(int k2 = 0;k2 < row;k2++){
                        if(sudokuRow[i][j][k1] == sudokuColumns[i][j][k2] && sudokuRow[i][j][k1] != '0'){
                            for(int k3 = 0; k3 < row;k3++){
                                if(sudokuRow[i][j][k1] == sudokuSquares[i][j][k3]){
                                    int counter = 0;
                                    boolean cotinuee = true;
                                    while(cotinuee){
                                        if(matrix[i][j][counter] == '0'){
                                            matrix[i][j][counter] = sudokuSquares[i][j][k3];
                                            cotinuee = false;
                                        }
                                        else{
                                            counter++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return matrix;
    }

    private void checkBlockNumbers(char[][] sudoku, char[][][] matrixRow, char[][][] matrixColumns) {
        //Metodo que servira para eliminar los numeros que no pueden ir en determinadas casillas por qué necesariamente van en otras.
        for (int squareI = 0; squareI < row; squareI += 3) {
            for (int squareJ = 0; squareJ < row; squareJ += 3) {
                //Miramos las casillas vacias en cada cuadrante
                int emptyCounter = 0;
                char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudoku[squareI + i][squareJ + j] == '0') {
                            emptyCounter++;
                        } else {
                            deleteNumberOfArray(sudoku[squareI + i][squareJ + j], numberArray);
                        }
                    }
                }

                if (emptyCounter == 2) {
                    //Miramos filas, si las dos casillas vacias se encuentran en la misma fila, esas han de tener las posiciones obligatorias
                    for (int i = 0; i < 3; i++) {
                        if (sudoku[squareI + i][squareJ + 0] == '0' && sudoku[squareI + i][1 + squareJ] == '0') {
                            makeRequired("rows", (squareI+i), numberArray, matrixRow, matrixRow[squareI + i][squareJ + 0], matrixRow[squareI + i][squareJ + 1]);
                        }else if (sudoku[squareI + i][squareJ + 1] == '0' && sudoku[squareI + i][2 + squareJ] == '0') {
                            makeRequired("rows", (squareI+i), numberArray, matrixRow, matrixRow[squareI + i][squareJ + 1], matrixRow[squareI + i][squareJ + 2]);
                        }else if (sudoku[squareI + i][squareJ + 0] == '0' && sudoku[squareI + i][2 + squareJ] == '0') {
                            makeRequired("rows", (squareI+i), numberArray, matrixRow, matrixRow[squareI + i][squareJ + 0], matrixRow[squareI + i][squareJ + 2]);
                        }
                    }
                    //Miramos columnas, si las dos casillas vacias se encuentran en la misma columna, esas han de tener las posiciones obligatorias
                    for (int i = 0; i < 3; i++) {
                        if (sudoku[squareI + 0][squareJ + i] == '0' && sudoku[squareI + 1][i + squareJ] == '0') {
                            makeRequired("columns", (squareJ+i), numberArray, matrixColumns, matrixColumns[squareI + 0][squareJ + i], matrixColumns[squareI + 1][i + squareJ]);
                        }else if (sudoku[squareI + 1][squareJ + i] == '0' && sudoku[squareI + 2][i + squareJ] == '0') {
                            makeRequired("columns", (squareJ+i), numberArray, matrixColumns, matrixColumns[squareI + 1][squareJ + i], matrixColumns[squareI + 2][squareJ + i]);
                        }else if (sudoku[squareI + 0][squareJ + i] == '0' && sudoku[squareI + 2][i + squareJ] == '0') {
                            makeRequired("columns", (squareJ+i), numberArray, matrixColumns, matrixColumns[squareI + 0][squareJ + i], matrixColumns[squareI + 2][i + squareJ]);
                        }
                    }
                }
                
                if(emptyCounter == 3){
                    //Miramos filas, si las tres casillas vacias se encuentran en la misma fila, esas han de tener las posiciones obligatorias
                    for(int i = 0;i < 3;i++){
                        if(sudoku[i+squareI][0+squareJ] == '0' && sudoku[i+squareI][1+squareJ] == '0' && sudoku[i+squareI][2+squareJ] == '0'){
                            makeRequired("rows", (i+squareI), numberArray, matrixRow, matrixRow[i+squareI][0+squareJ], matrixRow[i+squareI][1+squareJ], matrixRow[i+squareI][2+squareJ]);
                        }
                    }
                    
                    //Miramos columnas, si las tres casillas vacias se encuentran en la misma columna, esas han de tener las posiciones obligatorias
                    for(int i = 0;i < 3;i++){
                        if(sudoku[0+squareI][i+squareJ] == '0' && sudoku[1+squareI][i+squareJ] == '0' && sudoku[2+squareI][i+squareJ] == '0'){
                            makeRequired("columns", (squareJ+i), numberArray, matrixColumns, matrixColumns[0+squareI][i+squareJ], matrixColumns[1+squareI][i+squareJ],matrixColumns[2+squareI][i+squareJ]);
                        }
                    }
                }

            }
        }
    }

    private void makeRequired(String move, int row, char[] numberArray, char[][][] matrix, char[] pos1, char[] pos2) {
        if (move.equals("rows")) {
            //Eliminamos en todas las casillas los obligatorios
            for (int i = 0; i < row; i++) {
                if (numberArray[i] != '0') {
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < row; k++) {
                            if(matrix[row][j][k] == numberArray[i]){
                                matrix[row][j][k] = '0';
                            }
                        }
                    }
                }
            }
        }else if(move.equals("columns")){
            for (int i = 0; i < row; i++) {
                if (numberArray[i] != '0') {
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < row; k++) {
                            if(matrix[j][row][k] == numberArray[i]){
                                matrix[j][row][k] = '0';
                            }
                        }
                    }
                }
            }
        }
        
        //Ponemos en las casillas que toca los obligatorios
        for(int i = 0;i < row;i++){
            pos1[i] = numberArray[i];
            pos2[i] = numberArray[i];
        }
    }
    
    private void makeRequired(String move, int row, char[] numberArray, char[][][] matrix, char[] pos1, char[] pos2, char[] pos3) {
        if (move.equals("rows")) {
            //Eliminamos en todas las casillas los obligatorios
            for (int i = 0; i < row; i++) {
                if (numberArray[i] != '0') {
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < row; k++) {
                            if(matrix[row][j][k] == numberArray[i]){
                                matrix[row][j][k] = '0';
                            }
                        }
                    }
                }
            }
        }else if(move.equals("columns")){
            for (int i = 0; i < row; i++) {
                if (numberArray[i] != '0') {
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < row; k++) {
                            if(matrix[j][row][k] == numberArray[i]){
                                matrix[j][row][k] = '0';
                            }
                        }
                    }
                }
            }
        }
        
        //Ponemos en las casillas que toca los obligatorios
        for(int i = 0;i < row;i++){
            pos1[i] = numberArray[i];
            pos2[i] = numberArray[i];
            pos3[i] = numberArray[i];
        }
    }
    
    private void chooseByOcupations(char[][] sudoku, char[][][] matrix) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (isAlone(matrix[i][j])) {
                    sudoku[i][j] = matrix[i][j][0];
                } else {
                    sudoku[i][j] = '0';
                }
            }
        }
    }
    
    private boolean isAlone(char[] arr) {
        int counter = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != '0') {
                counter++;
            }
        }
        return (counter == 1) ? true : false;
    }
    
    private void chooseByLimit(char[][] sudoku, char[][][] matrix) {
        //Miramos en las filas primero.
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < row; k++) {
                    boolean itIs = false;
                    if (j == 0) {
                        for (int z = 1; z < row; z++) {
                            if(isInTheArray(matrix[i][z], matrix[i][j][k])) {
                                itIs = true;
                            }
                        }
                    } else {
                        for (int z = j + 1; z != j; z++) {
                            if (z == 9) {
                                z = 0;
                            }
                            if (isInTheArray(matrix[i][z], matrix[i][j][k])) {
                                itIs = true;
                            }
                        }
                    }
                    if (itIs == false && matrix[i][j][k] != '0') {
                        sudoku[i][j] = matrix[i][j][k];
                    }
                }
            }
        }

        //Miramos en las columanas
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < row; k++) {
                    boolean itIs = false;
                    if (j == 0) {
                        for (int z = 1; z < row; z++) {
                            if (isInTheArray(matrix[z][i], matrix[j][i][k])) {
                                itIs = true;
                            }
                        }
                    } else {
                        for (int z = j + 1; z != j; z++) {
                            if (z == 9) {
                                z = 0;
                            }
                            if (isInTheArray(matrix[z][i], matrix[j][i][k])) {
                                itIs = true;
                            }
                        }
                    }
                    if (itIs == false && matrix[i][j][k] != '0') {
                        sudoku[j][i] = matrix[j][i][k];
                    }
                }
            }
        }
        //Miramos en los cuadrados
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {

                for (int k = 0; k < 3; k++) {
                    for (int z = 0; z < 3; z++) {

                        for (int x = 0; x < row; x++) {
                            int cont = 0;
                            if (isInTheArray(matrix[i + 0][j + 0], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 0][j + 1], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 0][j + 2], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 1][j + 0], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 1][j + 1], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 1][j + 2], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 2][j + 0], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 2][j + 1], matrix[i + k][j + z][x])) {
                                cont++;
                            }
                            if (isInTheArray(matrix[i + 2][j + 2], matrix[i + k][j + z][x])) {
                                cont++;
                            }

                            if (cont == 1 && matrix[i + k][j + z][x] != '0') {
                                sudoku[i + k][j + z] = matrix[i + k][j + z][x];
                            }

                        }

                    }
                }

            }
        }
    }
    
    private boolean isInTheArray(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++) {
            if (c == arr[i]) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkValidSudoku(char[][] sudoku){
        //Filas
        for(int i = 0; i < row;i++){
            char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
            for(int j = 0; j < row;j++){
                if(sudoku[i][j] != '0'){
                    if(numberArray[Character.getNumericValue(sudoku[i][j]) - 1] == sudoku[i][j]){
                        deleteNumberOfArray(sudoku[i][j], numberArray);
                    }else{
                        return false;
                    }
                }
            }
        }
        
        //Columnas
        for(int i = 0; i < row;i++){
            char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
            for(int j = 0; j < row;j++){
                if(sudoku[j][i] != '0'){
                    if(numberArray[Character.getNumericValue(sudoku[j][i]) - 1] == sudoku[j][i]){
                        deleteNumberOfArray(sudoku[j][i], numberArray);
                    }else{
                        return false;
                    }
                }
            }
        }
        
        //Cuadrados
        for(int sqrI = 0; sqrI < row;sqrI += 3){
            for(int sqrJ = 0; sqrJ < row;sqrJ += 3){
                char[] numberArray = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
                for(int i = 0;i < 3;i++){
                    for(int j = 0;j<3;j++){
                        if(sudoku[i+sqrI][j+sqrJ] != '0'){
                            if(numberArray[Character.getNumericValue(sudoku[i+sqrI][j+sqrJ]) - 1] == sudoku[i+sqrI][j+sqrJ]){
                                deleteNumberOfArray(sudoku[i+sqrI][j+sqrJ], numberArray);
                            }else{
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private boolean equals(char[][] sud1, char[][]sud2){
        for(int i = 0; i < row; i++){
            for(int j = 0;j < row;j++){
                if(sud1[i][j] != sud2[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    
    private char[][] sudokuRandomMethod(char[][] sudoku, char[][][] sudokuMix){
        int iFinal = 0;
        int jFinal = 0;
        int maximum = 10;
        
        //Buscamos la casilla con el menor numero de posibilidades(min 2)
        for(int i = 0; i < row;i++){
            for(int j = 0;j < row;j++){
                int counter = 0;
                for(int k = 0;k < row;k++){
                    if(sudokuMix[i][j][k] != '0') counter++;
                }
                if(counter > 1 && counter < maximum){
                    maximum = counter;
                    iFinal = i;
                    jFinal = j;
                }
            }
        }
        //Cambiamos la casilla vacia por el numero correspondiente
        for(int k = 0;k < row;k++){
            if(sudokuMix[iFinal][jFinal][k] != '0'){
                char[][] changedSudoku = getSolution(changeSudoku(sudoku,iFinal,jFinal,sudokuMix[iFinal][jFinal][k]));
                if(changedSudoku != null){
                    return changedSudoku;
                }
            }
        }
        
        return null;
    }
    
    private char[][] changeSudoku(char[][] sud, int a, int b, char number){
        char[][] newSudoku = new char[row][row];
        
        for(int i = 0;i<row;i++){
            for(int j = 0;j<row;j++){
                newSudoku[i][j] = sud[i][j];
            }
        }
        
        newSudoku[a][b] = number;
        
        return newSudoku;
    }
    
    private void copy(char[][] original, char[][] copy){
        //Copiamos la matriz 
        for(int i = 0; i < row; i++){
            for(int j = 0;j < row;j++){
                copy[i][j] = original[i][j];
            }
        }
    }
    
    private boolean checkGameOver(char[][] sud){
        for(int i = 0; i < row; i++){
            for(int j = 0;j < row;j++){
                if(sud[i][j] == '0'){
                    return false;
                }
            }
        }
        return true;
    }
    
    
    
}
