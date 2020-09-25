--
-- Base de datos: `integracionrj`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `HOTDRINK`
--

CREATE TABLE `HOTDRINK` (
  `name` varchar(40) NOT NULL,
  `stock` int(255) NOT NULL
);

--
-- Volcado de datos para la tabla `HOTDRINK`
--

INSERT INTO `HOTDRINK` (`name`, `stock`) VALUES
('chocolate', 25),
('chocolate', 25),
('cafe', 25),
('cafe', 25),
('te', 0),
('te', 0);
COMMIT;
