<?php
 
/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data
 
  /**
 * check for POST request 
 */
 
//  file_put_contents('post_data.txt',print_r($_POST,true));
 
if (isset($_REQUEST['tag']) && $_REQUEST['tag'] != '') {
    // get tag
    $tag = $_REQUEST['tag'];
 
    // include DB_function
    require_once 'DB_Functions.php';
    $db = new DB_Functions();
 
    // response Array
    $response = array("tag" => $tag, "error" => FALSE);
 
    // checking tag
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_REQUEST['email'];
        $password = $_REQUEST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        // file_put_contents('resp_db.txt',json_encode($user));
        if (is_array($user)) {
            // user found
            $response["error"] = FALSE;
            $response["uid"] = $user["id"];
            $response["user"]["email"] = $user["email"];
			$response["user"]["password"] = $user["password"];
            echo json_encode($response);
            // file_put_contents('resp_data1.txt',json_encode($response));
        } else {
            // user not found
            // echo json with error = 1
            $response["uid"] = 0;
            $response["error"] = TRUE;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);            
            // file_put_contents('resp_data0.txt',json_encode($response));
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $username = $_POST['username'];
		$firstname = $_POST['firstname'];
		$lastname = $_POST['lastname'];
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = TRUE;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($username, $firstname, $lastname, $password, $email, $created_at);
            if ($user) {
                // user stored successfully
                $response["error"] = FALSE;
                $response["uid"] = $user["uid"];
                $response["user"]["username"] = $user["username"];
				$response["user"]["firstname"] = $user["firstname"];
				$response["user"]["lastname"] = $user["lastname"];
				$response["user"]["password"] = $user["password"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = TRUE;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } else {
        // user failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown 'tag' value. It should be either 'login' or 'register'";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>