/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocne;

import com.grupa1.dbconnection.DBUtil;
import java.sql.Connection;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class ServisKonekcija extends Service<Connection> {

    @Override
    protected Task createTask() {
        return new Task<Connection>() {
            @Override
            protected Connection call() throws Exception {
                return DBUtil.napraviKonekciju();
            }
        };
    }
    
}
