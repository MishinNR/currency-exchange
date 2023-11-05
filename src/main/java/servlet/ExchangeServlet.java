package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDTO;
import dto.ExchangeDTO;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import static util.ValidationUtil.*;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService;
    private final ObjectMapper objectMapper;

    public ExchangeServlet() {
        this.exchangeService = ExchangeService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String fromCurrency = req.getParameter("from");
                String toCurrency = req.getParameter("to");
                String amount = req.getParameter("amount");

                validateRequestParametersArePresent(fromCurrency, toCurrency, amount);
                validateParameterCode(fromCurrency);
                validateParameterCode(toCurrency);
                validateParameterAmount(amount);

                ExchangeDTO exchangeDTO = exchangeService.exchange(
                        fromCurrency.toUpperCase(),
                        toCurrency.toUpperCase(),
                        new BigDecimal(amount)
                );
                objectMapper.writeValue(writer, exchangeDTO);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
            }
        }
    }
}
