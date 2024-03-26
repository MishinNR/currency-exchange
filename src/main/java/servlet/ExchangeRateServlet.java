package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDto;
import dto.ExchangeRateDto;
import entity.ExchangeRate;
import exception.ApplicationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;
import util.ExchangeRateModelMapper;
import util.validation.FormFieldValidator;
import util.validation.PathValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService;
    private final FormFieldValidator formFieldValidator;
    private final PathValidator pathValidator;
    private final ObjectMapper objectMapper;
    private final ExchangeRateModelMapper exchangeRateModelMapper;

    public ExchangeRateServlet() {
        this.exchangeRateService = ExchangeRateService.getInstance();
        this.exchangeRateModelMapper = ExchangeRateModelMapper.getInstance();
        this.formFieldValidator = FormFieldValidator.getInstance();
        this.pathValidator = PathValidator.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String path = req.getPathInfo();
                pathValidator.validatePathWithCurrencyPairCode(path);

                String pairCode = path.replaceAll("/", "");
                formFieldValidator.validatePairCode(pairCode);

                String baseCode = pairCode.substring(0, 3);
                String targetCode = pairCode.substring(3, 6);

                ExchangeRateDto exchangeRateDto = exchangeRateService.findByBaseAndTargetCodes(baseCode, targetCode);
                objectMapper.writeValue(writer, exchangeRateDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }
        doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String path = req.getPathInfo();
                pathValidator.validatePathWithCurrencyPairCode(path);

                String pairCode = path.replaceAll("/", "");
                formFieldValidator.validatePairCode(pairCode);

                String parametersLine = req.getReader().readLine();
                pathValidator.validatePathWithParameters(parametersLine);
                String[] parameters = parametersLine.split("&");

                String rateParamDesignation = "rate=";
                String rate = Arrays.stream(parameters)
                        .filter(param -> param.startsWith(rateParamDesignation))
                        .findFirst()
                        .map(param -> param.replace(rateParamDesignation, ""))
                        .orElse("");
                formFieldValidator.validateRate(rate);

                String baseCode = pairCode.substring(0, 3);
                String targetCode = pairCode.substring(3, 6);

                ExchangeRate exchangeRate = exchangeRateModelMapper.convertToEntity(exchangeRateService.findByBaseAndTargetCodes(baseCode, targetCode));
                exchangeRate.setRate(new BigDecimal(rate));
                ExchangeRateDto updatedExchangeRateDto = exchangeRateService.update(exchangeRate);
                objectMapper.writeValue(writer, updatedExchangeRateDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }
}
