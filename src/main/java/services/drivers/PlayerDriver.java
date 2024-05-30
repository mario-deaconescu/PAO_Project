package services.drivers;

import utility.Column;
import utility.ColumnValue;
import utility.PasswordHash;
import exceptions.InvalidLoginException;
import utility.query.ColumnObject;
import utility.query.QueryClause;

import javax.annotation.Nullable;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class PlayerDriver extends DriverBase {

    private static final PlayerDriver instance = new PlayerDriver();

    private String name = null;

    private PlayerDriver() {
        super("players", Arrays.asList(
                new Column("username", Column.ColumnType.TEXT, "UNIQUE"),
                new Column("salt", Column.ColumnType.TEXT, "NOT NULL"),
                new Column("password", Column.ColumnType.TEXT, "NOT NULL"),
                new Column("balance", Column.ColumnType.REAL, "DEFAULT 0")
        ));
    }

    public static PlayerDriver getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    private PasswordHash hashPassword(String password, @Nullable String inputSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        if (inputSalt != null) {
            salt = Base64.getDecoder().decode(inputSalt);
        } else {
            random.nextBytes(salt);
        }
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder();
        return new PasswordHash(enc.encodeToString(hash), enc.encodeToString(salt));
    }

    public void login(String username, String password) throws InvalidLoginException {
        var salt = select(List.of(new Column("salt", Column.ColumnType.TEXT)), new QueryClause("username = ?", new ColumnObject[]{new ColumnObject(username, Column.ColumnType.TEXT)}));
        if (salt.isEmpty()) {
            throw new InvalidLoginException("User not found");
        }
        try {
            var hash = hashPassword(password, (String) salt.getFirst().get("salt"));
            var user = select(List.of(new Column("username", Column.ColumnType.TEXT)),
                    new QueryClause("username = ? AND password = ?",
                            new ColumnObject[]{
                                    new ColumnObject(username, Column.ColumnType.TEXT),
                                    new ColumnObject(hash.hash(), Column.ColumnType.TEXT)
                            }
                    )
            );
            if (user.isEmpty()) {
                throw new InvalidLoginException("Invalid password");
            }
            this.name = user.getFirst().get("username").toString();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void register(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var hash = hashPassword(password, null);
        insert(Arrays.asList(
                new ColumnValue("username", username),
                new ColumnValue("password", hash.hash()),
                new ColumnValue("salt", hash.salt()),
                new ColumnValue("balance", 0)
        ));
        this.name = username;
    }

    public void logout() {
        this.name = null;
    }

    public double getBalance() {
        if (name == null) {
            throw new RuntimeException("User not logged in");
        }
        var balance = select(List.of(new Column("balance", Column.ColumnType.REAL)), new QueryClause("username = ?", new ColumnObject[]{
                new ColumnObject(name, Column.ColumnType.TEXT)
        }));
        if (balance.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return (double) balance.getFirst().get("balance");
    }

    public void updateBalance(double amount) {
        if (name == null) {
            throw new RuntimeException("User not logged in");
        }
        update(List.of(new ColumnValue("balance", amount)),
                new QueryClause("username = ?",
                        new ColumnObject[]{
                                new ColumnObject(name, Column.ColumnType.TEXT)
                        }));
    }

    public List<String> getAllUsernames() {
        return select(List.of(new Column("username", Column.ColumnType.TEXT)), null).stream()
                .map(row -> row.get("username").toString())
                .toList();
    }
}
