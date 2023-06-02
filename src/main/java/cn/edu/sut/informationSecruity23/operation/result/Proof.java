package cn.edu.sut.informationSecruity23.operation.result;

import it.unisa.dia.gas.jpbc.Element;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proof {
    private Element phi_1;
    private Element phi_2;
    private Element phi_3;
    private Element credAgg;
    private Element c;
    private Element W_u;
    private Element W_f;
    private Element W_k;
    private Element W_o;
    private Element W_beta;
    private Element W_ou;
    private String[][] attributes;
    private String msg;

    //debug
    private Element D1;
    private Element D2;
    private Element D3;
    private Element D4;
}
