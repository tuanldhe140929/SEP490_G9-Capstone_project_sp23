export class Account{
    id: number;
    accountCreatedDate: Date;
    accountLastModified: Date;
    email: string;
    enabled: boolean;
    constructor(){
        this.id = -1;
        this.accountCreatedDate = new Date();
        this.accountLastModified = new Date();
        this.email = "";
        this.enabled = true;
    }
}