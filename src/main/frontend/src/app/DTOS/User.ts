import { Role } from "./Role";


export class User{

  id: number
  email: string;
  enabled: boolean;
  role: Role[];
  username: string;
  firstName: string;
  lastName: string;
  emailVerified:boolean;
  avatar: string;
  createdDate: Date;
  lastModified: Date;

  constructor() {
    this.firstName ="";
    this.lastName ="";
    this.id = -1;
    this.email = '';
    this.enabled = true;
    this.role = [];
    this.username = "";
    this.emailVerified = false;
    this.avatar = "";
    this.createdDate = new Date;
    this.lastModified = new Date;
  }
} 
