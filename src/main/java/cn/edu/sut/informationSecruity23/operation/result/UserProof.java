package cn.edu.sut.informationSecruity23.operation.result;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

@Data
public class UserProof {
    private Element uvk;
    private Element HashedR;
    private Element Sb;
}
