export class User{
    id: number;
    fname: string;
    lname: string;
    username: string;
    email: string;
    phone: string;
    memSince: string = "today";

    constructor(
        id: number,
        fname: string,
        lname: string,
        username: string,
        email: string,
        phone: string,
        memSince: string,  
    ){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.memSince = memSince;
    }

}
