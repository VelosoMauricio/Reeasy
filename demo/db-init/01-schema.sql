-- Este script se ejecutará DENTRO de la base de datos que Docker ya creó.
-- Solo contiene las sentencias para crear las tablas.

-- -----------------------------------------------------
-- Table `Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Users` ;

CREATE TABLE IF NOT EXISTS `Users` (
  `user_id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `lastname` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `addres` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Roles` ;

CREATE TABLE IF NOT EXISTS `Roles` (
  `rol_id` INT NOT NULL,
  `rol_name` VARCHAR(45) NULL,
  PRIMARY KEY (`rol_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `UsersDetails`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UsersDetails` ;

CREATE TABLE IF NOT EXISTS `UsersDetails` (
  `user_id` INT NOT NULL,
  `rol_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `rol_id`),
  INDEX `fk_Users_has_Roles_Roles1_idx` (`rol_id` ASC) VISIBLE,
  INDEX `fk_Users_has_Roles_Users_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Users_has_Roles_Users`
    FOREIGN KEY (`user_id`)
    REFERENCES `Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_has_Roles_Roles1`
    FOREIGN KEY (`rol_id`)
    REFERENCES `Roles` (`rol_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Enterprise`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Enterprise` ;

CREATE TABLE IF NOT EXISTS `Enterprise` (
  `name` VARCHAR(50) NOT NULL,
  `cuit` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`cuit`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Coupons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Coupons` ;

CREATE TABLE IF NOT EXISTS `Coupons` (
  `coupon_id` INT NOT NULL,
  `expiration_date` DATE NOT NULL,
  `price` INT NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `amount` INT NOT NULL,
  `link` VARCHAR(45) NULL,
  `image` BLOB NOT NULL,
  `Enterprise_cuit` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`coupon_id`, `Enterprise_cuit`),
  INDEX `fk_Coupons_Enterprise1_idx` (`Enterprise_cuit` ASC) VISIBLE,
  CONSTRAINT `fk_Coupons_Enterprise1`
    FOREIGN KEY (`Enterprise_cuit`)
    REFERENCES `Enterprise` (`cuit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Bottles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Bottles` ;

CREATE TABLE IF NOT EXISTS `Bottles` (
  `bottle_id` INT NOT NULL,
  `plastic_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`bottle_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Scans`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Scans` ;

CREATE TABLE IF NOT EXISTS `Scans` (
  `user_id` INT NOT NULL,
  `bottle_id` INT NOT NULL,
  `amount` INT NULL,
  `image` BLOB NULL,
  `timestamp` TIMESTAMP NULL,
  PRIMARY KEY (`user_id`, `bottle_id`),
  INDEX `fk_Users_has_Bottles_Bottles1_idx` (`bottle_id` ASC) VISIBLE,
  INDEX `fk_Users_has_Bottles_Users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Users_has_Bottles_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_has_Bottles_Bottles1`
    FOREIGN KEY (`bottle_id`)
    REFERENCES `Bottles` (`bottle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RedeemCoupons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RedeemCoupons` ;

CREATE TABLE IF NOT EXISTS `RedeemCoupons` (
  `coupon_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `timestamp` TIMESTAMP NULL,
  PRIMARY KEY (`coupon_id`, `user_id`),
  INDEX `fk_Coupons_has_Users_Users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_Coupons_has_Users_Coupons1_idx` (`coupon_id` ASC) VISIBLE,
  CONSTRAINT `fk_Coupons_has_Users_Coupons1`
    FOREIGN KEY (`coupon_id`)
    REFERENCES `Coupons` (`coupon_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Coupons_has_Users_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;