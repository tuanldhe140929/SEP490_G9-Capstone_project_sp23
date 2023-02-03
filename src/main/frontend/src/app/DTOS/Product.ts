

import { ProductFile } from "./ProductFile";
import { Tag } from "./Tag";
import { Type } from "./Type";
import { User } from "./User";

export class Product {
  id: number;
  name: string;
  url: string;
  description: string;
  uploadedDate: Date | undefined;
  lastUpdated: Date | undefined;
  price: number;
  coverImage: string;
  details: string;
  files: ProductFile[];
  user: User;	
  tags: Tag[];
  type: Type;

  constructor(){
    this.id = -1;
    this.name = "";
    this.url = "";
    this.description = "";
    this.price = 0;
    this.user = new User();
    this.type = new Type();
    this.files = [];
    this.tags = [];
    this.coverImage = "";
    this.details = "";
}
}
