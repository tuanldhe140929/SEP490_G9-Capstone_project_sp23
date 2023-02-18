export class User{
	id:number;
	email:string;
	username:string;
	enabled:boolean;
	verifed:boolean;
  role: string;
  image: string;
  joinedDate: Date;
  constructor() {
    this.joinedDate = new Date;
    this.id = -1;
    this.email = "";
    this.username = "";
    this.enabled = false;
    this.verifed = false;
    this.role = "";
    this.image ="";
  }
} 
