package me.jasper.jasperproject.Util.Commands;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.jasper.jasperproject.Util.Util;

public class CalculatorMath implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        try {
            if (args != null)
                sender.sendMessage("Hasil: " + calculate(String.join("", args)));
            return true;
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (sender instanceof Player p) {
                p.sendMessage(Util.deserialize("<red><b>ERROR MATH!</b> " + msg));
                return false;
            }
            sender.sendMessage("ERROR MATH! " + msg);
        }
        return false;

    }

    public static String calculate(String input) {
        return BigDecimal.valueOf(new Parser(preprocess(input)).parse())
                .setScale(15, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();
    }

    private static String preprocess(String input) {
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d+)?)(k|jt|m|t)", Pattern.CASE_INSENSITIVE)
                .matcher(input);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            double val = Double.parseDouble(m.group(1));
            switch (m.group(2).toLowerCase()) {
                case "k":
                    val *= 1_000;
                    break; // ribu
                case "jt":
                    val *= 1_000_000;
                    break; // juta
                case "m":
                    val *= 1_000_000_000;
                    break; // milyar
                case "t":
                    val *= 1_000_000_000_000L;
                    break; // triliun
            }
            m.appendReplacement(sb, BigDecimal.valueOf(val)
                    .stripTrailingZeros()
                    .toPlainString());
        }
        m.appendTail(sb);
        return handlePercent(
                sb.toString()
                        .replace(",", ".")
                        .replace("×", "*")
                        .replace("÷", "/")
                        .replace("∛", "cbrt")
                        .replace("√", "sqrt")
                        .replaceAll("(?<=[0-9)!])(?=[(A-Za-z])", "*")//
        );
    }

    private static String handlePercent(String s) {
        Pattern pMulDiv = Pattern.compile("([^)]+?)([*/])(\\d+(?:\\.\\d+)?)%");
        Matcher m1;
        while ((m1 = pMulDiv.matcher(s)).find()) {
            String leftExpr = m1.group(1);
            String op = m1.group(2);
            String pctNum = m1.group(3);

            double leftVal;
            try {
                leftVal = new Parser(leftExpr).parse();
            } catch (Exception e) {
                break;
            }
            BigDecimal leftBd = BigDecimal.valueOf(leftVal);
            BigDecimal pctBd = new BigDecimal(pctNum)
                    .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);

            BigDecimal res = op.equals("*")
                    ? leftBd.multiply(pctBd)
                    : leftBd.divide(pctBd, MathContext.DECIMAL128);

            String rep = res.stripTrailingZeros().toPlainString();
            s = m1.replaceFirst(Matcher.quoteReplacement(rep));
        }
        return s.replaceAll("([+-])(\\d+(?:\\.\\d+)?)%", "* (1$1$2/100)")
                .replaceAll("\\(([^)]+)\\)%", "($1/100)")
                .replaceAll("(?<![A-Za-z0-9_\\.])" + "(\\d+(?:\\.\\d+)?)%", "($1/100)");
    }

    private static class Parser {
        private final String expr;
        private int pos = -1, ch;

        Parser(String s) {
            this.expr = s;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < expr.length()) {
                throw new RuntimeException("Not support/wrong args: '" + (char) ch + "' char at " + (pos + 1));
            }
            return x;
        }

        private void nextChar() {
            ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
        }

        private boolean eat(int charToEat) {
            while (ch == ' ')
                nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        private double parseExpression() {
            double x = parseTerm();
            while (true) {
                if (eat('+'))
                    x += parseTerm();
                else if (eat('-'))
                    x -= parseTerm();
                else
                    return x;
            }
        }

        private double parseTerm() {
            double x = parseFactor();
            while (true) {
                if (eat('*'))
                    x *= parseFactor();
                else if (eat('/')) {
                    double d = parseFactor();
                    if (d == 0)
                        throw new RuntimeException("Cant divide zero/'0'");
                    x /= d;
                } else if (eat(';')) {
                    x %= parseFactor();
                } else
                    return x;
            }
        }

        private double parseFactor() {
            if (eat('+'))
                return parseFactor();
            if (eat('-'))
                return -parseFactor();

            double x;
            int startPos = this.pos;

            if (eat('(')) {
                x = parseExpression();
                if (!eat(')'))
                    throw new RuntimeException("There is no close parentheses");
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.')
                    nextChar();
                x = Double.parseDouble(expr.substring(startPos, pos));
            } // beyond these until while is operator
            else if (isFunction("sqrt"))
                x = applyFunc(Math::sqrt);
            else if (isFunction("cbrt"))
                x = applyFunc(Math::cbrt);
            else if (isFunction("flr"))
                x = Math.floor(parseParensAfter());
            else if (isFunction("rnd"))
                x = Math.round(parseParensAfter());
            else if (isFunction("sin"))
                x = Math.sin(parseParensAfter());
            else if (isFunction("cos"))
                x = Math.cos(parseParensAfter());
            else if (isFunction("abs"))
                x = Math.abs(parseParensAfter());
            else if (isFunction("tan"))
                x = Math.tan(parseParensAfter());
            else if (isFunction("ceil"))
                x = applyFunc(Math::ceil);
            else if (isFunction("log"))
                x = Math.log(parseParensAfter());
            else if (isFunction("rabs"))
                x = -applyFunc(Math::abs);
            else if (isFunction("rev"))
                x = -parseParensAfter();
            else
                throw new RuntimeException("Not support/wrong args: '" + (char) ch + "' char at " + pos);

            while (eat('!')) {
                if (x < 0 || x != Math.floor(x))
                    throw new RuntimeException("Illegal factorial");
                if (x > 10)
                    throw new RuntimeException("Factor is too large");
                x = factorial((int) x);
            }

            if (eat('^')) {
                x = Math.pow(x, parseFactor());
            }

            return x;
        }

        private boolean isFunction(String name) {
            if (expr.startsWith(name, pos)) {
                pos += name.length() - 1;
                nextChar();
                return true;
            }
            return false;
        }

        private double parseParensAfter() {
            if (!eat('('))
                throw new RuntimeException("Missing args front parentheses");
            double v = parseExpression();
            if (!eat(')'))
                throw new RuntimeException("Missing args close parentheses");
            return v;
        }

        private double applyFunc(java.util.function.DoubleUnaryOperator func) {
            return func.applyAsDouble(parseParensAfter());
        }

        private int factorial(int n) {
            int f = 1;
            for (int i = 2; i <= n; i++)
                f *= i;
            return f;
        }
    }
}
