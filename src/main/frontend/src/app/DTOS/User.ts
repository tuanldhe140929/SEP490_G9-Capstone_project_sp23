import { Role } from "./Role";


export class User{

  id: number
  email: string;
  accountCreatedDate: Date;
  accountLastModified: Date;
  enabled: boolean;
  role: Role[];
  username: string;
  firstname: string;
  lastname: string;
  emailVerified:boolean;
  image: string;
  joinedDate: Date;
  userCreatedDate: Date;
  userLastModified: Date;

  constructor() {
    this.firstName ="";
    this.lastName ="";
    this.userCreatedDate = new Date;
    this.id = -1;
    this.email = '';
    this.accountCreatedDate = new Date;
    this.accountLastModified = new Date;
    this.enabled = true;
    this.role = [];
    this.username = "";
    this.firstname = "";
    this.lastname = "";
    this.emailVerified = false;
    this.image = "";
    this.userCreatedDate = new Date();
    this.userLastModified = new Date();
  }
} 
