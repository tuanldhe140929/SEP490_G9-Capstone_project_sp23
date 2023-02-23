import { Role } from "./Role";


export class User{

  id: number
  email: string;
  accountCreatedDate: Date;
  accountLastModified: Date;
  enabled: boolean;
  role: Role[];
  username: string;
  firstName: string;
  lastName: string;
  emailVerified:boolean;
  avatar: string;
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
    this.firstName = "";
    this.lastName = "";
    this.emailVerified = false;
    this.avatar = "";
    this.userCreatedDate = new Date();
    this.userLastModified = new Date();
  }
} 
