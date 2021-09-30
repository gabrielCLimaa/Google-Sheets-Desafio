import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import entity.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Test.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "17x8ObHeTbjf5TaYVIQ0KEXR0xwH1xo8gUd3F2MK6i3A";
        final String range = "A4:I";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            ArrayList<Student> stdList = getStudenstList(values);
            ArrayList<Student> result = getResult(stdList);
            result.forEach(System.out::println);

            int L = 4;

            for(int i = 0; i < result.size();i++) {
                ValueRange appendBody = new ValueRange()
                        .setValues(Arrays.asList(
                                Arrays.asList(result.get(i).getSituation(),result.get(i).getGradeForFinalTest())
                        ));

                service.spreadsheets().values()
                        .update(spreadsheetId, "G"+L+":H", appendBody)
                        .setValueInputOption("RAW")
                        .execute();
                L++;
            }
        }
    }

    public static ArrayList<Student> getStudenstList(List<List<Object>> list) {
        ArrayList<Student> studentsList= new ArrayList<>();
        for(List row : list) {
            Student student = new Student(String.valueOf(row.get(0)),String.valueOf(row.get(1)),String.valueOf(row.get(2)),
                    String.valueOf(row.get(3)),String.valueOf(row.get(4)),String.valueOf(row.get(5)),"", "");
            studentsList.add(student);
        }
        return studentsList;
    }

    public static ArrayList<Student> getResult(ArrayList<Student> students) {
        for (Student student : students) {
            double media = (Double.valueOf(student.getP1()) + Double.valueOf(student.getP2())
                    + Double.valueOf(student.getP3())) / 3.0;
            
            if (Integer.valueOf(student.getAbsences()) > (60 * 0.25)) {
                student.setSituation("Reprovado por Falta");
                student.setGradeForFinalTest("0");
            } else if (media < 50.00) {
                student.setSituation("Reprovado por Nota");
                student.setGradeForFinalTest("0");
            }  else if (media >= 50.00 && media < 70.00) {
                student.setSituation("Exame Final");
                double n = (100.00 - media);
                student.setGradeForFinalTest(String.valueOf(n));
            } else {
                student.setSituation("Aprovado");
                student.setGradeForFinalTest("0");
            }
        }
        return students;
    }
}


