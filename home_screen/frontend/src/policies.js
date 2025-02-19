const policies = () => {
    const lowercase = /[a-z]/;
    const uppercase = /[A-Z]/;
    const digits = /[0-9]/;
    const special = /[!@#$%^&*(),.?":{}|<>]/;
    const forbidden = /[\s\\/]/;

    const passwdPolicy = (password) => {
        if (
            password.length >= 8 &&
            lowercase.test(password) &&
            uppercase.test(password) &&
            digits.test(password) &&
            special.test(password) &&
            !forbidden.test(password)
        ){
            return "correct";
        }
        if(forbidden.test(password)){
            return "Password contains forbidden characters.";
        }
        return "Password must have: At least 8 characters, one uppercase, one lowercase, one digit and one special character."
    }

    const loginPolicy = (login) => {
        if(
            login.length >= 4 &&
            !forbidden.test(login)
        )
        {
            return "correct";
        }
        if(forbidden.test(login)){
            return "Login contains forbidden characters";
        }
        return "Login must contain at least 4 characters";
    }

    return {passwdPolicy, loginPolicy};
}
export default policies;