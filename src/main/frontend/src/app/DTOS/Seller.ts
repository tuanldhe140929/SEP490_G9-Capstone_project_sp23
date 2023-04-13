
import { Role } from "./Role";

export class Seller{
  id:number;
	email:string;
	createdDate:Date
	lastModified:Date
	enabled:boolean
	roles: Role[];
	username:string;
	firstName:string;
	lastName:string;
	avatar:string;
	emailVerified:boolean;
	phoneNumber:string;
	sellerEnabled:boolean;

  constructor() {
    this.id = -1;
    this.email = "";
    this.createdDate = new Date;
    this.lastModified = new Date;
    this.enabled = true;
    this.roles = [];
    this.username = "";
    this.firstName = "";
    this.lastName = "";
    this.avatar="";
    this.emailVerified = true;
    this.phoneNumber = "";
    this.sellerEnabled = true;

  }
}
