import { Typography, useTheme, Toolbar } from "@mui/material";
import { Box } from "@mui/system";
import Brand from "../../images/brand-unisinos.png"


interface ILayoutBaseWelcomeProps {
    children: React.ReactNode;
}

export const LayoutBaseWelcome: React.FC<ILayoutBaseWelcomeProps> = ({ children }) => {
    const theme = useTheme();

    return (
        <>
            <Box height={theme.spacing(12)} color="white" display="flex" flexDirection="column" gap={1} bgcolor={theme.palette.background.default}>
                <Box display="flex" alignItems="center" justifyContent="center" paddingY={2}>
                    <Typography variant="h1">Uni4Life</Typography>
                </Box>
            </Box>
            <Box flex={1}>
                {children}
            </Box>
            <footer style={{height: theme.spacing(12), backgroundColor: theme.palette.background.default}}>
                <Toolbar sx={{ display: "flex", justifyContent: "center", height: "100%" }}>
                    <img src={Brand} alt="Logo Unisinos" style={{height: "100%"}}/>
                    <Typography variant="body1" color="white" textAlign="center">
                    &copy; {new Date().getFullYear()} Uni4Life
                    </Typography>
                </Toolbar>
            </footer>
        </>
    )
}