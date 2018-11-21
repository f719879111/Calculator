package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "Caculator";
    TextView txt_result, txt_edit;
    boolean isOperateDown = false;//运算符是否已经按过一次，默认没有按过 false
    boolean isDotDown = false;//. 是否已经按过一次，默认没有按过 false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_edit = (TextView)findViewById(R.id.txt_edit);
        txt_result = (TextView)findViewById(R.id.txt_result);

        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_multi).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_sub).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_e2).setOnClickListener(this);
        findViewById(R.id.btn_e8).setOnClickListener(this);
        findViewById(R.id.btn_e16).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                num_down("0");break;
            case R.id.btn_1:
                num_down("1");break;
            case R.id.btn_2:
                num_down("2");break;
            case R.id.btn_3:
                num_down("3");break;
            case R.id.btn_4:
                num_down("4");break;
            case R.id.btn_5:
                num_down("5");break;
            case R.id.btn_6:
                num_down("6");break;
            case R.id.btn_7:
                num_down("7");break;
            case R.id.btn_8:
                num_down("8");break;
            case R.id.btn_9:
                num_down("9");break;
            case R.id.btn_plus:
                operator_down("+");break;
            case R.id.btn_sub:
                operator_down("-");break;
            case R.id.btn_divide:
                operator_down("÷");break;
            case R.id.btn_multi:
                operator_down("×");break;
            case R.id.btn_clear:
                isDotDown = false;
                isOperateDown = false;
                txt_edit.setText("0");
                txt_result.setText("");
                break;
            case R.id.btn_back: {
                String strEdit = txt_edit.getText().toString();
                int length = strEdit.length();
                if (Pattern.matches("^=[0-9].*", strEdit)) {
                    txt_edit.setText("0");
                    txt_result.setText("");
                } else {
                    if (length > 0) {
                        String word = strEdit.substring(length - 1, length);
                        if(word.equals("."))
                            isDotDown = false;
                        if(word.equals("+") || word.equals("-") || word.equals("×") || word.equals("÷"))
                            isOperateDown = false;
                        txt_edit.setText(strEdit.substring(0, length - 1));
                    }
                }
                break;
            }
            case R.id.btn_dot: {
                String strEdit = txt_edit.getText().toString();
                if (!isDotDown) {
                    isDotDown = true;
                    if(Pattern.matches("^=[0-9].*", strEdit))
                        strEdit = "0";
                    txt_edit.setText(strEdit + ".");
                }
                break;
            }
            case R.id.btn_equal:
                equal();break;
            case R.id.btn_e2:
                change2();break;
            case R.id.btn_e8:
                change8();break;
            case R.id.btn_e16:
                change16();break;
        }
    }

    //数字按钮活动
    private void num_down(String num) {
        String strEdit = txt_edit.getText().toString();
        isOperateDown = false;

        if (strEdit.equals("0") || Pattern.matches("^=[0-9].*", strEdit)) {
            txt_edit.setText(num);
            txt_result.setText("");
        } else {
            txt_edit.setText(strEdit + num);
        }
    }

    // 运算符活动
    private void operator_down(String operator) {
        if(!isOperateDown) {
            String strEdit = txt_edit.getText().toString();
            isOperateDown = true;
            isDotDown = false;
            if(Pattern.matches("^=[0-9].*", strEdit))
                strEdit = strEdit.substring(1, strEdit.length());
            txt_edit.setText(strEdit + operator);
        }
    }
    //进制转换符号
    private void change2(){
        String strEdit = txt_edit.getText().toString();
        int i = Integer.parseInt(strEdit);
        String s = Integer.toBinaryString(i);
        strEdit = s;
        txt_edit.setText(strEdit);
    }

    private void change8(){
        String strEdit = txt_edit.getText().toString();
        int i = Integer.parseInt(strEdit);
        String s = Integer.toOctalString(i);
        strEdit = s;
        txt_edit.setText(strEdit);
    }

    private void change16(){
        String strEdit = txt_edit.getText().toString();
        int i = Integer.parseInt(strEdit);
        String s = Integer.toHexString(i);
        strEdit = s;
        txt_edit.setText(strEdit);
    }

    private void equal() {
        String strEdit = txt_edit.getText().toString();
        int length = strEdit.length();
        if(!Pattern.matches("^=[0-9].*", strEdit))
        {
            txt_result.setText(strEdit);
            if(Pattern.matches(".*[\\+\\-\\×\\÷\\.]$", strEdit)) {
                strEdit = strEdit.substring(0, length - 1);
            }
            String postfixExp = getPostfixExp(strEdit);
            txt_edit.setText("=" + calPostfix(postfixExp));
        }
    }


    //将中缀表达式转换为后缀表达式
    private String getPostfixExp(String str) {
        String postfix = "";
        String numString = "";   //因数字不止一位，所以使用String存储
        Stack numStack = new Stack();
        Stack opStack = new Stack();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(Character.isDigit(ch) || ch == '.') {    //判定ch是否为数字或者是小数点
                numString += String.valueOf(ch);        //将数字和小数点放入numString，等待下一个运算符
            } else {    //ch为运算符时
                if(numString.length() > 0) {
                    numStack.push(numString);//将此运算符前的数字压入数字栈
                    numString = "";         //压入栈后，重置numString
                }
                opPush(opStack, numStack, ch);
            }
        }

        //判定numString是否为空，因为最后一个可能是数字，没有运算符进行判定
        if(numString.length() > 0)
            numStack.push(numString);

        //检查完后，将运算符栈中转入到数字栈中
        while(!opStack.empty()) {
            numStack.push(opStack.pop());
        }
        //将数字栈打印出来得到后缀表达式
        //通过两个栈的先进后出特点，得到栈的逆序
        while(!numStack.empty()) {
            opStack.push(numStack.pop());
        }
        while(!opStack.empty()) {
            postfix = postfix + String.valueOf(opStack.pop()) + " ";
        }
        return postfix;
    }

    //后缀表达式运算
    private String calPostfix(String str) {
        String result = "";
        Stack numStack = new Stack();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(ch == ' ') {
                //运算符时
                if(result.length() > 0 && (result.equals("+") || result.equals("-") || result.equals("×") || result.equals("÷")))
                {
                    double num = 0;
                    double secondNum = Double.parseDouble(String.valueOf(numStack.pop()));
                    double firstNum = Double.parseDouble(String.valueOf(numStack.pop()));
                    switch (result) {
                        case "+":
                            num = firstNum + secondNum;break;
                        case "-":
                            num = firstNum - secondNum;break;
                        case "×":
                            num = firstNum * secondNum;break;
                        case "÷":
                            num = firstNum / secondNum;break;
                    }
                    numStack.push(num);
                }
                else if(result.length() > 0) {
                    numStack.push(result);
                }
                result = "";
            } else {
                result += String.valueOf(ch);
            }
        }
        return BigDecimal.valueOf(Double.valueOf(String.valueOf(numStack.pop()))).stripTrailingZeros().toPlainString();
    }
    //获取运算符权重
    private int getOpWeight(char ch) {
        // + - 权重为1
        if(ch == '+' || ch == '-') return 1;
        //× ÷ 权重为2
        if(ch == '×' || ch == '÷') return 4;
        return -1;
    }

    //将运算符压入栈
    private void opPush(Stack opStack, Stack numStack, char ch) {
        if(canOpPush(opStack, ch)) {    //判定能否将运算符压入栈内
            opStack.push(ch);           //true则压入栈内
        } else {                        //false（即待压入运算符优先级<=栈顶运算符优先级）
            //将栈顶运算符取出压入数字栈
            numStack.push(String.valueOf(opStack.pop()));
            //此处需要递归判定，弹出所有优先级>=该运算符的栈顶元素
            opPush(opStack, numStack, ch);
        }
    }

    //判定运算符能否压入运算符栈
    private Boolean canOpPush(Stack opStack, char ch) {
        //当运算符栈为空时，返回true；或当待压入运算符权重大于栈顶权重，返回true
        if(opStack.empty() || (getOpWeight(ch) > getOpWeight(String.valueOf(opStack.peek()).charAt(0))))
            return true;

        return false;           //其他情况返回false
    }
}
