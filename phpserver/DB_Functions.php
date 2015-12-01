<?php
 
class DB_Functions {
 
    private $db;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
        
    }
 
    /**
     * Store user details
     */
    public function storeUser($username, $firstname, $lastname, $password, $email, $created_at) {
        $uid = uniqid('', true);
        //$hash = $this->hashSSHA($password);
        //$encrypted_password = $hash["encrypted"]; // encrypted password
        //$salt = $hash["salt"]; // salt
        $result = mysqli_query($this->db->con,
		"INSERT INTO member(username, firstname, lastname, password, email, created_at) 
		VALUES('$username', '$firstname', '$lastname', '$password','$email', NOW())");
        // check for result
        if ($result) {
            // gettig the details
            $uid = mysqli_insert_id($this->db->con); // last inserted id
            $result = mysqli_query($this->db->con,"SELECT * FROM member WHERE id = $uid");
            // return details
            return mysqli_fetch_array($result);
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($username, $password) {
        $result = mysqli_query($this->db->con,"select * from member where username='$username' and password='$password'") or die(mysqli_connect_errno());
        // check for result 
        $no_of_rows = mysqli_num_rows($result);
        if ($no_of_rows > 0) {
            $user = $result->fetch_array(MYSQLI_ASSOC);
			   return array("id" => $user['id'],
                            "email" => $user['username'],
                            "password" => $user['password']
             );
			}else{
				return false;
			}
		}
 
    /**
     * Check user is exist or not
     */
    public function isUserExisted($email) {
        $result = mysqli_query($this->db->con,"SELECT email from member WHERE email = '$email'");
        $no_of_rows = mysqli_num_rows($result);
        if ($no_of_rows > 0) {
            // user exis
            return true;
        } else {
            // user not exist
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
	 /*
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    } */
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    /*public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }*/
 
}
 
?>