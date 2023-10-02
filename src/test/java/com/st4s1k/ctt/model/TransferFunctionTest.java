package com.st4s1k.ctt.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TransferFunctionTest {

    @Test
    void testTransferFunction() {

        Polynomial b = Polynomial.of(9, 0);
        Polynomial a = Polynomial.of(50, 3).plus(15, 2).plus(1, 1);
        TransferFunction h = TransferFunction.of(b, a);

        /*
                        9                    9
         H(s) = ------------------ = -----------------
                s(10s + 1)(5s + 1)   50s^3 + 15s^2 + s
        */

        assertThat(h)
                .hasToString("( 9 ) / ( 50s^3 + 15s^2 + s ) ");
        assertThat(h.pid(0.0056, 0, 0.056))
                .hasToString("( 0.504s + 0.05 ) / ( 50s^3 + 15s^2 + s ) ");
        assertThat(h.pid(0.0056, 0, 0.056).feedback())
                .hasToString("( 0.504s + 0.05 ) / ( 50s^3 + 15s^2 + 1.504s + 0.05 ) ");
        assertThat(h.normalize())
                .hasToString("( 0.18 ) / ( s^3 + 0.3s^2 + 0.02s ) ");
        assertThat(h.normalize().pid(0.0056, 0, 0.056))
                .hasToString("( 0.01s + 0.001 ) / ( s^3 + 0.3s^2 + 0.02s ) ");
        assertThat(h.normalize().pid(0.0056, 0, 0.056).feedback())
                .hasToString("( 0.01s + 0.001 ) / ( s^3 + 0.3s^2 + 0.03s + 0.001 ) ");

        log.info("                  H(s) = {}", h);
        log.info("              H.PID(s) = {}", h.pid(0.0056, 0, 0.056));
        log.info("     H.PID.feedback(s) = {}", h.pid(0.0056, 0, 0.056).feedback());
        log.info("             H.norm(s) = {}", h.normalize());
        log.info("         H.norm.PID(s) = {}", h.normalize().pid(0.0056, 0, 0.056));
        log.info("H.norm.PID.feedback(s) = {}", h.normalize().pid(0.0056, 0, 0.056).feedback());

        /*
            Output:
                              H(s) = ( 9 ) / ( 50s^3 + 15s^2 + s )
                          H.PID(s) = ( 0.504s + 0.05 ) / ( 50s^3 + 15s^2 + s )
                 H.PID.feedback(s) = ( 0.504s + 0.05 ) / ( 50s^3 + 15s^2 + 1.504s + 0.05 )
                         H.norm(s) = ( 0.18 ) / ( s^3 + 0.3s^2 + 0.02s )
                     H.norm.PID(s) = ( 0.01s + 0.001 ) / ( s^3 + 0.3s^2 + 0.02s )
            H.norm.PID.feedback(s) = ( 0.01s + 0.001 ) / ( s^3 + 0.3s^2 + 0.03s + 0.001 )
        */
    }
}
