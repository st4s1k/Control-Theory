package com.st4s1k.ctt.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class PolynomialTest {

    @Test
    void testPolynomial() {
        Polynomial zero = Polynomial.zero();
        Polynomial p = Polynomial.of(4, 3).plus(3, 2).plus(1, 0); // 4x^3 + 3x^2 + 1
        Polynomial q = Polynomial.of(1, 1).plus(3, 0); // x + 3
        Polynomial r = p.plus(q);
        Polynomial s = p.times(q);
        Polynomial t = p.evaluate(q);
        Polynomial pqDivQuotient = p.div(q)[0];
        Polynomial pqDivReminder = p.div(q)[1];

        log.info("zero(x)         = {}", zero);
        log.info("p(x)            = {}", p);
        log.info("q(x)            = {}", q);
        log.info("quo(p/q)        = {}", pqDivQuotient);
        log.info("quo(p/q).coef() = {}", pqDivQuotient.coefficient());
        log.info("quo(p/q).deg()  = {}", pqDivQuotient.degree());
        log.info("rem(p/q)        = {}", pqDivReminder);
        log.info("rem(p/q).coef() = {}", pqDivReminder.coefficient());
        log.info("rem(p/q).deg()  = {}", pqDivReminder.degree());
        log.info("p(3)            = {}", p.evaluate(3));
        log.info("p'(x)           = {}", p.differentiate());
        log.info("p''(x)          = {}", p.differentiate().differentiate());
        log.info("p(x) + q(x)     = {}", r);
        log.info("p(x) * q(x)     = {}", s);
        log.info("p(q(x))         = {}", t);
        log.info("0 - p(x)        = {}", zero.minus(p));

        /*
            zero(x)         = 0
            p(x)            = 4s^3 + 3s^2 + 1
            q(x)            = s + 3
            quo(p/q)        = 4s^2 - 9s + 27
            quo(p/q).coef() = 4.0
            quo(p/q).deg()  = 2
            rem(p/q)        = -80
            rem(p/q).coef() = -80.0
            rem(p/q).deg()  = 0
            p(3)            = 136.0
            p'(x)           = 12s^2 + 6s
            p''(x)          = 24s + 6
            p(x) + q(x)     = 4s^3 + 3s^2 + s + 4
            p(x) * q(x)     = 4s^4 + 15s^3 + 9s^2 + s + 3
            p(q(x))         = 4s^3 + 39s^2 + 126s + 136
            0 - p(x)        = -4s^3 - 3s^2 - 1
        */

        assertThat(zero.isNotZero()).isFalse();
        assertThat(zero.isZero()).isTrue();
        assertThat(zero.isConstant()).isTrue();
        assertThat(zero.doubleValue()).isZero();
        assertThat(zero).hasToString("0");
        assertThat(p).hasToString("4s^3 + 3s^2 + 1");
        assertThat(q).hasToString("s + 3");
        assertThat(r).hasToString("4s^3 + 3s^2 + s + 4");
        assertThat(s).hasToString("4s^4 + 15s^3 + 9s^2 + s + 3");
        assertThat(t).hasToString("4s^3 + 39s^2 + 126s + 136");
        assertThat(zero.minus(p)).hasToString("-4s^3 - 3s^2 - 1");
        assertThat(p.evaluate(3)).isEqualTo(136.0);
        assertThat(p.differentiate()).hasToString("12s^2 + 6s");
        assertThat(p.differentiate().differentiate()).hasToString("24s + 6");
        assertThat(pqDivQuotient).hasToString("4s^2 - 9s + 27");
        assertThat(pqDivQuotient.degree()).isEqualTo(2);
        assertThatThrownBy(pqDivQuotient::doubleValue)
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Polynomial is not a constant");
        assertThat(pqDivReminder).hasToString("-80");
        assertThat(pqDivReminder.degree()).isZero();
        assertThat(pqDivReminder.doubleValue()).isEqualTo(-80);
    }
}
