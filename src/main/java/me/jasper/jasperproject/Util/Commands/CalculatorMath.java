package me.jasper.jasperproject.Util.Commands;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CalculatorMath implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        try {
            if (args != null)
                sender.sendMessage("Hasil: " + mathcalculatorcalculate(String.join("", args)));
        } catch (RuntimeException e) {
            switch (e.getMessage()) {
                case "uns":
                    sender.sendMessage("tidak support/salah argumen");
                    break;
                case "largefac":
                    sender.sendMessage(
                            "Faktorial terlalu besar, coba angka yang lebih kecil dari 20");
                    break;
                case "dcf":
                    sender.sendMessage(
                            "Faktorial hanya bisa untuk angka bulat positif, coba angka yang lebih besar dari 0");
                    break;
                case "2.,":
                    sender.sendMessage(
                            "Hanya satu titik desimal yang diperbolehkan, coba lagi");
                    break;
                case "dv0":
                    sender.sendMessage(
                            "Pembagian dengan 0 tidak diperbolehkan");
                    break;
                default:
                    sender.sendMessage("sesuatu ada yang salah...");
                    break;
            }
        }
        return false;
    }

    public static String mathcalculatorcalculate(String expressionInput) {
        String preprocessedExpression = preprocessExpression(expressionInput);
        StringBuilder expression = new StringBuilder(
                preprocessedExpression
                        .replaceAll(" ", "")
                        .replaceAll(",", ".")
                        .replaceAll("×", "*")
                        .replaceAll("÷", "/")
                        .replaceAll("∛", "cbrt")
                        .replaceAll("√", "sqrt"));

        double result = evalBasicMath(expression);
        BigDecimal bd = BigDecimal.valueOf(result)
                .setScale(15, RoundingMode.HALF_UP)
                .stripTrailingZeros();

        return bd.toPlainString();
    }

    private static String expandNumberSuffixes(String input) {
        Pattern p = Pattern.compile("(\\d+(?:\\.\\d+)?)(k|jt|m|t)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String numberStr = m.group(1);
            String suffix = m.group(2).toLowerCase();
            double val = Double.parseDouble(numberStr);
            switch (suffix) {
                case "k": // ribu
                    val *= 1_000;
                    break;
                case "jt": // juta
                    val *= 1_000_000;
                    break;
                case "m": // milyar
                    val *= 1_000_000_000;
                    break;
                case "t": // triliun
                    val *= 1_000_000_000_000L;
                    break;
            }
            String replacement = BigDecimal.valueOf(val)
                    .stripTrailingZeros()
                    .toPlainString();
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String preprocessExpression(String expressionInput) {
        String expandedSuffix = expandNumberSuffixes(expressionInput);
        StringBuilder result = new StringBuilder();
        char prev = '\0';
        for (int i = 0; i < expandedSuffix.length(); i++) {
            char curr = expandedSuffix.charAt(i);
            if (prev != '\0') {
                if ((Character.isDigit(prev) && (curr == '(' || Character.isLetter(curr)))
                        || ((prev == '!' || prev == ')')
                                && (curr == '(' || Character.isLetter(curr) || Character.isDigit(curr)))) {
                    result.append('*');
                }
            }
            result.append(curr);
            prev = curr;
        }
        return handlePercentage(result.toString());
    }

    private static String handlePercentage(String preprocessed) {
        Pattern mulOrDivPattern = Pattern.compile("(\\S+?)\\s*([*/])\\s*(\\d+(?:\\.\\d+)?)%");
        boolean found = true;
        while (found) {
            Matcher m = mulOrDivPattern.matcher(preprocessed);
            if (m.find()) {
                String leftOperand = m.group(1);
                String op = m.group(2);
                String pctNumber = m.group(3);

                boolean hasLeadingParen = false;
                if (leftOperand.startsWith("(")) {
                    hasLeadingParen = true;
                    leftOperand = leftOperand.substring(1);
                }

                double leftValue;
                try {
                    leftValue = evalBasicMath(new StringBuilder(leftOperand));
                } catch (Exception e) {
                    break;
                }
                BigDecimal bigLeft = BigDecimal.valueOf(leftValue);
                BigDecimal percentage = new BigDecimal(pctNumber)
                        .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);
                BigDecimal result;
                if (op.equals("*")) {
                    result = bigLeft.multiply(percentage);
                } else {
                    result = bigLeft.divide(percentage, MathContext.DECIMAL128);
                }
                String replacement;
                if (hasLeadingParen) {
                    replacement = "(" + result.stripTrailingZeros().toPlainString();
                } else {
                    replacement = result.stripTrailingZeros().toPlainString();
                }
                preprocessed = m.replaceFirst(Matcher.quoteReplacement(replacement));
            } else {
                found = false;
            }
        }

        preprocessed = handleAddSubPercentage(preprocessed);

        preprocessed = handleParenPct(preprocessed);

        Pattern leftoverPct = Pattern.compile("(?<!\\))(\\d+(?:\\.\\d+)?)%");
        Matcher leftoverMatcher = leftoverPct.matcher(preprocessed);
        StringBuilder sb2 = new StringBuilder();
        while (leftoverMatcher.find()) {
            String pctNumber = leftoverMatcher.group(1);
            leftoverMatcher.appendReplacement(sb2, "(" + pctNumber + "/100)");
        }
        leftoverMatcher.appendTail(sb2);
        preprocessed = sb2.toString();

        preprocessed = preprocessed.replaceAll("\\)\\s*%", ")/100");

        return preprocessed;
    }

    private static String handleAddSubPercentage(String expr) {
        Pattern pattern = Pattern.compile("([+-])\\s*(\\d+(?:\\.\\d+)?)%");
        Matcher matcher = pattern.matcher(expr);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String operator = matcher.group(1);
            String number = matcher.group(2);
            String replacement = String.format("* (1 %s %s/100)", operator, number);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String handleParenPct(String expr) {
        int index = expr.indexOf("%)");
        while (index != -1) {
            int openIndex = findMatchingOpenParen(expr, index - 1);
            if (openIndex == -1)
                break;
            expr = expr.substring(0, openIndex)
                    + "("
                    + expr.substring(openIndex + 1, index)
                    + "/100)"
                    + expr.substring(index + 2);
            index = expr.indexOf("%)");
        }
        return expr;
    }

    private static int findMatchingOpenParen(String expr, int closePos) {
        int level = 0;
        for (int i = closePos; i >= 0; i--) {
            if (expr.charAt(i) == ')')
                level++;
            else if (expr.charAt(i) == '(') {
                if (level == 0)
                    return i;
                level--;
            }
        }
        return -1;
    }

    private static double evalBasicMath(StringBuilder expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length())
                    handleUnsupportedCharacter((char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+'))
                        x += parseTerm();
                    else if (eat('-'))
                        x -= parseTerm();
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*'))
                        x *= parseFactor();
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0)
                            throw new RuntimeException("dv0");
                        x /= divisor;
                    } else if (eat(';'))
                        x %= parseFactor();
                    else
                        return x;
                }
            }

            double parseFactor() {
                if (eat('+'))
                    return parseFactor();
                if (eat('-'))
                    return -parseFactor();

                double x;
                int startPos = this.pos;

                if (ch == '(' && startPos > 0 && Character.isDigit(expression.charAt(startPos - 1))) {
                    expression.insert(startPos, "*");
                    nextChar();
                }

                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                    if (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                        expression.insert(pos, "*");
                        nextChar();
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    int dotCount = 0;
                    int numberStart = pos;
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        if (ch == '.')
                            dotCount++;
                        nextChar();
                    }
                    String numStr = expression.substring(numberStart, pos);
                    if (dotCount > 1)
                        throw new RuntimeException("2.,");
                    x = Double.parseDouble(numStr);
                } else if (isMatch("sqrt")) {
                    pos += 3;
                    nextChar();
                    if (!eat('('))
                        handleUnsupportedCharacter('(');
                    x = Math.sqrt(parseExpression());
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                } else if (isMatch("cbrt")) {
                    pos += 3;
                    nextChar();
                    if (!eat('('))
                        handleUnsupportedCharacter('(');
                    x = Math.cbrt(parseExpression());
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                } else if (isMatch("flr")) {
                    pos += 2;
                    nextChar();
                    if (!eat('('))
                        handleUnsupportedCharacter('(');
                    x = Math.floor(parseExpression());
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                } else if (isMatch("rnd")) {
                    pos += 2;
                    nextChar();
                    if (!eat('('))
                        handleUnsupportedCharacter('(');
                    x = Math.round(parseExpression());
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                } else if (isMatch("sin")) {
                    pos += 2;
                    nextChar();
                    if (!eat('('))
                        handleUnsupportedCharacter('(');
                    x = Math.sin(parseExpression());
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                } else if (isMatch("cos")) {
                    pos += 2;
                    nextChar();
                    if (!eat('('))
                        handleUnsupportedCharacter('(');
                    x = Math.cos(parseExpression());
                    if (!eat(')'))
                        handleUnsupportedCharacter(')');
                } else {
                    handleUnsupportedCharacter((char) ch);
                    return 0;
                }

                while (eat('!')) {
                    if (x < 0 || x != Math.floor(x))
                        throw new RuntimeException("dcf");
                    if (x > 20)
                        throw new RuntimeException("largefac");
                    x = factorial(x);
                }

                if (eat('^')) {
                    double exponent = parseFactor();
                    x = Math.pow(x, exponent);
                }

                return x;
            }

            private double factorial(double n) {
                double result = 1;
                for (int i = 2; i <= (int) n; i++)
                    result *= i;
                return result;
            }

            private boolean isMatch(String func) {
                return expression.substring(pos).startsWith(func);
            }

            private void handleUnsupportedCharacter(char c) {
                System.out.println("unsupported " + c);
                throw new RuntimeException("uns");
            }
        }.parse();
    }
}
