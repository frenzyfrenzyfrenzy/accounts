package com.svintsov.accounts.validator;

import com.svintsov.accounts.AccountsProperties;
import com.svintsov.accounts.model.TransferRequest;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

public class Validator {

    private final Schema transferRequestSchema;

    public Validator(AccountsProperties accountsProperties) {
        InputStream requestSchemaInputStream = Validator.class.getResourceAsStream(accountsProperties.getRequestSchemaPath());
        JSONObject rawRequestSchema = new JSONObject(new JSONTokener(requestSchemaInputStream));
        transferRequestSchema = SchemaLoader.load(rawRequestSchema);
    }

    public List<String> validateTransferRequest(TransferRequest transferRequest) {
        try {
            transferRequestSchema.validate(new JSONObject(transferRequest));
        } catch (ValidationException validationException) {
            return validationException.getAllMessages();
        }
        return null;
    }

}
