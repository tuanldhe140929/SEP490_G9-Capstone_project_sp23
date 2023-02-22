export class User{
	id:number;
  firstName: string;
  lastName: string;
	email:string;
	username:string;
	enabled:boolean;
	verifed:boolean;
  role: string;
  avatar: string;
  userCreatedDate: Date;
  constructor() {
    this.firstName ="";
    this.lastName ="";
    this.userCreatedDate = new Date;
    this.id = -1;
    this.email = "";
    this.username = "";
    this.enabled = false;
    this.verifed = false;
    this.role = "";
    this.avatar ="";
  }
} 
