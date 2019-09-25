package applicationsfx;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.regex.Pattern;



public class Calculator {

    //карта приоритетов
    private static HashMap<String,Integer> priorityMap = new HashMap(){{
        put("(", 0);
        put("(", 0);
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
        put("%", 2);
        put("<", 3);
        put("++",3);
        put("--",3);
    }};

    private Calculator() {
    }




    public static String calculate(String expression){
        if (expression == null) return null;
        BigDecimal result;
        ArrayList<String> postFixExpression;
        try {
            postFixExpression = (ArrayList<String>) toPostfix(expression);
            if (postFixExpression == null) return null;
        } catch (IllegalExpressionException e) {
            return "null";
        }
        result = calculatePostFixExpression(postFixExpression);

        if (result != null) {
            result = result.setScale(2, BigDecimal.ROUND_HALF_UP);
            return result.toString();
        } else return "null";
    }

    private static List<String> toPostfix(String expression) throws IllegalExpressionException{
        //заменяем все унарные минусы на "<"
        expression = replaceSingleMinus(expression);

        Stack<String> stack = new Stack();
        ArrayList<String> elements = (ArrayList<String>) splitToParts(expression);
        ArrayList<String> postFixElements = new ArrayList<>();

        for (String element : elements){
            if (!Pattern.matches("\\(|\\)|\\+|-|\\*|/|<|%", element)) {
                //текущий эллемент - число
                //число добавляем в стек
                postFixElements.add(element);
            } else {
                if (element.equals("("))
                    stack.push(element);
                else if (element.equals(")")){
                    //при закрывающей скобке
                    while (true){
                        String elementFromStack;
                        try {
                            //достаем эллемент из стэка
                            elementFromStack = stack.pop();
                        } catch (EmptyStackException e){
                            //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                            return null;
                        }
                        if (elementFromStack.equals("("))
                            //если эллемент открывающая скобка, завершаем цикл
                            break;
                        else
                            postFixElements.add(elementFromStack);
                    }
                } else {
                    if (stack.isEmpty())
                        //Если стек пуст, записываем в него знак
                        stack.push(element);
                    else {
                        while (true) {
                            if (stack.isEmpty()){
                                //Если стек пуст, записываем в него знак
                                stack.push(element);
                                break;
                            } else {
                                try {
                                    if (isHigherPriority(element, stack.peek())) {
                                        //Если приоритетет текущего оператора выше
                                        //приоритета оператора на вершине стэка
                                        //записываем в стэк оператор
                                        stack.push(element);
                                        break;
                                    } else {
                                        //Иначе достаем из стэка оператор, дописываем его в выражение
                                        postFixElements.add(stack.pop());
                                    }
                                } catch (EmptyStackException e) {
                                    //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                                    //Ловим соответствующее исключение, возвращаем null
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        while (!stack.isEmpty()){
            //Оставшиеся в стэке эллементы
            //дописываем в выражение
            postFixElements.add(stack.pop());
        }
        return postFixElements;
    }


    private static BigDecimal calculatePostFixExpression(List<String> expression){
        BigDecimal result;
        Stack<BigDecimal> decimals = new Stack<>();

        for (String element : expression) {
            //перебираем эллементы выражения
            if (Pattern.matches("\\(|\\)|\\+|-|\\*|/|%", element)){

                // достаем из стэка два числа
                BigDecimal decimal1 = null, decimal2 = null;
                try {
                    decimal1 = decimals.pop();
                    decimal2 = decimals.pop();
                } catch (EmptyStackException e){
                    //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                    //Ловим соответствующее исключение, возвращаем null
                    return null;
                }

                // производим между числами соответствующее действие
                if (element.equals("+")){
                    decimals.push(decimal2.add(decimal1));
                } else if (element.equals("-")) {
                    decimals.push(decimal2.subtract(decimal1));
                } else if (element.equals("*")) {
                    decimals.push(decimal2.multiply(decimal1));
                } else if (element.equals("/")) {
                    try {
                        decimals.push(decimal2.divide(decimal1, MathContext.DECIMAL32));
                    } catch (ArithmeticException e){
                        //при делении на ноль возвращаем null
                        return null;
                    }

                } else if (element.equals("%")) {
                try {
                    decimals.push(decimal2.remainder(decimal1));
                } catch (ArithmeticException e){
                    //при делении на ноль возвращаем null
                    return null;
                }

            }

            }

            else if (element.equals("<")) {
                //текущий элемент - унарный минус
                // достаем из стэка число
                BigDecimal decimal = null;
                try {
                    decimal = decimals.pop();
                } catch (EmptyStackException e){
                    //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                    //Ловим соответствующее исключение, возвращаем null
                    return null;
                }
                //Кладем обратно в стек число с обратным знаком
                decimals.push(decimal.multiply(new BigDecimal("-1")));
            } else {
                // текущий эллемент - оператор, записываем его в стэк
                BigDecimal decimal;
                try {
                    decimal = new BigDecimal(element);
                } catch (NumberFormatException e){

                    return null;
                }
                decimals.push(decimal);
            }
        }


        result = decimals.pop();
        if (decimals.isEmpty())
            return result;
        else
            return null;
    }

    private static boolean isHigherPriority(String element1, String element2){
        return priorityMap.get(element1) > priorityMap.get(element2);
    }


    private static List<String> splitToParts(String expression) throws IllegalExpressionException {
        ArrayList<String> partsOfExpression = new ArrayList<>();

        //удаляем все пробелы
        expression = expression.replaceAll(" ", "");
        //разбиваем строку на массив символов
        String[] chars = expression.split("");

        String bufferForNumber = "";
        for (int i = 0; i < chars.length; i++) {
            //перебираем символы
            if (!"".equals(chars[i]) && Pattern.matches("\\(|\\)|\\+|-|\\*|/|<|%", chars[i])){

                if (!bufferForNumber.equals("")) {
                    partsOfExpression.add(bufferForNumber);
                    bufferForNumber = "";
                }
                partsOfExpression.add(chars[i]);
            } else if (Pattern.matches("[\\d.]", String.valueOf(chars[i]))){
                //если цифра или знак разделителя
                bufferForNumber += chars[i];
            } else {
                if (!"".equals(chars[i]))
                    throw new IllegalExpressionException();
            }
        }

        if (!bufferForNumber.equals("")) partsOfExpression.add(bufferForNumber);

        return partsOfExpression;
    }


    private static String replaceSingleMinus(String expression){
        String res = expression.replaceAll("^-d*","<");
        res = res.replaceAll("\\(-","(<");
        return res;
    }

}