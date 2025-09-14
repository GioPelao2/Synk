import Image from "next/image";

interface LogoProps {
    src: string; 
    alt: string;
    width?: number;
    height?: number;
}
export default function Logo({ src, alt, width = 120, height = 50 }: LogoProps) {
    return (
        <Image
            src = {src}
            alt = {alt}
            width={width}
            height={height}
        />    
    );
}

