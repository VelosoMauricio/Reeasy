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
  `address` VARCHAR(45) NULL,
  `points` INT NULL,
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
  `coupon_id` INT NOT NULL AUTO_INCREMENT,
  `expiration_date` DATE NOT NULL,
  `price` INT NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `amount` INT NOT NULL,
  `link` VARCHAR(45) NULL,
  `image` LONGBLOB NOT NULL,
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
  `image` LONGBLOB NULL,
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`user_id`, `bottle_id`, `timestamp`),
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
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`coupon_id`, `user_id`, `timestamp`),
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

-- -----------------------------------------------------
-- Table `Contexts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Contexts` ;

CREATE TABLE IF NOT EXISTS `Contexts` (
  `context` VARCHAR(512) NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_Contexts_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Messages` ;

CREATE TABLE IF NOT EXISTS `Messages` (
  `messages` VARCHAR(512) NOT NULL,
  `es_chat` TINYINT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_Messages_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- INSERTS DE PRUEBA

INSERT INTO Users (user_id, name, lastname, email, address, points)
VALUES
(1, 'Juan', 'Pérez', 'juan.perez@example.com', 'Calle Falsa 123', 200),
(2, 'Ana', 'Gómez', 'ana.gomez@example.com', 'Av. Siempreviva 742', 10),
(3, 'Luis', 'Martínez', 'luis.martinez@example.com', 'San Martín 456', 34000);

-- Roles
INSERT INTO Roles (rol_id, rol_name)
VALUES
(1, 'Admin'),
(2, 'User'),
(3, 'Collector');

-- UsersDetails
INSERT INTO UsersDetails (user_id, rol_id)
VALUES
(1, 1), -- Juan es Admin
(2, 2), -- Ana es User
(3, 2); -- Luis es User

-- Enterprise
INSERT INTO Enterprise (name, cuit)
VALUES
('EcoPlast SA', '30-12345678-9'),
('GreenRecycle SRL', '30-98765432-1');

-- Bottles
INSERT INTO Bottles (bottle_id, plastic_type)
VALUES
(1, 'PEAD'),
(2, 'PEBD'),
(3, 'PET');

-- Coupons
INSERT INTO Coupons (coupon_id, expiration_date, price, description, amount, link, image, Enterprise_cuit)
VALUES
(1, '2025-12-31', 100, 'Descuento en supermercado', 50, 'http://example.com/coupon1', 0x1234, '30-12345678-9'),
(2, '2025-11-30', 200, '2x1 en cine', 30, 'http://example.com/coupon2', 0x5678, '30-98765432-1');
