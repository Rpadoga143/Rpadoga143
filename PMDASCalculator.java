import java.util.Scanner;

public class PMDASCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a mathematical expression:");
        String expression = scanner.nextLine();


                 
        double result = evaluateExpression(expression);
        System.out.println("Result: " + result);
               

        scanner.close();
    }

    public static double evaluateExpression(String expression) {
        // Remove spaces from the input expression
        expression = expression.replaceAll("\\s+", "");
        
        try {
            // Use Java's built-in eval() function to evaluate the expression
            return eval(expression);
        } catch (Exception e) {
            System.err.println("Error: Invalid expression.");
            return Double.NaN; // Return NaN for invalid expressions
        }
    }

    public static double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
             
                if (pos < expression.length()) {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) {
                        x += parseTerm(); // Addition
                    } else if (eat('-')) {
                        x -= parseTerm(); // Subtraction
                    } else {
                        return x;
                    }
                }
   

            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) {
                        x *= parseFactor(); // Multiplication
                    } else if (eat('/')) {
                        x /= parseFactor(); // Division
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // Unary plus
                if (eat('-')) return parseFactor(); // Unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // Parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // Numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                return x;
            }

            boolean eat(int expected) {
           if (ch == expected) {
                    nextChar();
                    return true;
                }
                return false;
            }
        }.parse();
    }
}