
import { User } from "./User";

export class Product {
  id: number;
  name: string;
  url: string;
  description: string;
  uploadedDate: Date | undefined;
  lastUpdated: Date | undefined;
  price: number;
  coverImage:string;
  user: User;	
  tags: string[];
  type: string;

  constructor(){
    this.id = -1;
    this.name = "";
    this.url = "";
    this.description = "";
    this.price = 0;
    this.user = new User();
    this.type = "";
    this.tags = [];
    this.coverImage = "";

}
}
