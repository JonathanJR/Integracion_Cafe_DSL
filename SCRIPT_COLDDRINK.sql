--
-- Base de datos: `integracionrj`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `COLDDRINK`
--

CREATE TABLE `COLDDRINK` (
  `name` varchar(255) NOT NULL,
  `stock` int(255) NOT NULL
);

--
-- Volcado de datos para la tabla `COLDDRINK`
--

INSERT INTO `COLDDRINK` (`name`, `stock`) VALUES
('cola', 25),
('fanta', 25),
('agua', 25),
('cola', 25),
('fanta', 25),
('agua', 25);
COMMIT;
